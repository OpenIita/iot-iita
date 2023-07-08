/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comp.emqx;


import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.thing.ThingService;
import cc.iotkit.common.utils.SpringUtils;
import cc.iotkit.converter.Device;
import cc.iotkit.converter.DeviceMessage;
import cc.iotkit.data.manager.IDeviceInfoData;
import cc.iotkit.data.manager.IProductModelData;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.model.product.ProductModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TransparentConverter {

    private final Map<String, IScripter> scripters = new HashMap<>();
    private final Map<String, String> scripts = new HashMap<>();

    private IDeviceInfoData deviceInfoData;
    private IProductModelData productModelData;

    /**
     * 透传解码
     */
    public ThingModelMessage decode(TransparentMsg msg) {
        String productKey = msg.getProductKey();
        productKey = checkScriptUpdate(productKey, msg.getModel());
        msg.setProductKey(productKey);
        ThingModelMessage decodeMsg = scripters.get(productKey).decode(msg);
        decodeMsg.setProductKey(msg.getProductKey());
        decodeMsg.setDeviceName(msg.getDeviceName());
        return decodeMsg;
    }

    /**
     * 透传编码
     */
    public DeviceMessage encode(ThingService<?> service, Device device) {
        String productKey = service.getProductKey();
        checkScriptUpdate(productKey, device.getModel());
        TransparentMsg transparentMsg = scripters.get(productKey).encode(service);
        //转换成网关消息
        String deviceName = service.getDeviceName();
        DeviceInfo gateway = getGatewayInfo(productKey, deviceName);
        DeviceMessage message = new DeviceMessage();
        message.setProductKey(gateway.getProductKey());
        message.setDeviceName(gateway.getDeviceName());
        message.setMid(transparentMsg.getMid());
        //透传格式消息内容,mac、model、data
        message.setContent(transparentMsg);
        return message;
    }

    private ProductModel getScript(String model) {
        if (productModelData == null) {
            productModelData = SpringUtils.getBean("productModelDataCache");
        }

        return productModelData.findByModel(model);
    }

    private DeviceInfo getGatewayInfo(String subPk, String subDn) {
        if (deviceInfoData == null) {
            deviceInfoData = SpringUtils.getBean("deviceInfoDataCache");
        }

        String parentId = deviceInfoData.findByProductKeyAndDeviceName(subPk, subDn).getParentId();
        return deviceInfoData.findByDeviceId(parentId);
    }

    /**
     * 检查产品脚本是否更新
     */
    private String checkScriptUpdate(String pk, String model) {
        ProductModel productModel = null;
        if (StringUtils.isNotBlank(model)) {
            productModel = getScript(model);
        }
        //指定型号获取不到获取默认型号
        if (productModel == null && StringUtils.isNotBlank(pk)) {
            productModel = getScript(ProductModel.getDefaultModel(pk));
        }
        if (productModel == null) {
            throw new BizException(ErrCode.MODEL_SCRIPT_NOT_FOUND);
        }

        String productKey = productModel.getProductKey();
        String script = productModel.getScript();

        String oldScript = scripts.get(productKey);
        if (script.equals(oldScript)) {
            return productKey;
        }

        String type = productModel.getType();
        if (ProductModel.TYPE_LUA.equals(type)) {
            scripters.putIfAbsent(productKey, new LuaScripter(productModel));
        } else if (ProductModel.TYPE_JS.equals(type)) {
            scripters.putIfAbsent(productKey, new JsScripter(productModel));
        }

        //更新脚本
        IScripter scripter = scripters.get(productKey);
        scripter.setScript(script);
        scripts.put(productKey, script);
        return productKey;
    }

}
