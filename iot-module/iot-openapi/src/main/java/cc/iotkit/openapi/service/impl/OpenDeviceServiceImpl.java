package cc.iotkit.openapi.service.impl;

import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.DeviceUtil;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.manager.IDeviceInfoData;
import cc.iotkit.data.manager.IProductData;
import cc.iotkit.data.manager.IThingModelData;
import cc.iotkit.manager.dto.vo.thingmodel.ThingModelVo;
import cc.iotkit.manager.service.DeviceService;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.product.ThingModel;
import cc.iotkit.openapi.dto.bo.device.OpenapiDeviceBo;
import cc.iotkit.openapi.dto.vo.OpenDevicePropertyVo;
import cc.iotkit.openapi.service.OpenDeviceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

import static cc.iotkit.common.enums.ErrCode.DEVICE_NOT_FOUND;
import static cc.iotkit.common.enums.ErrCode.DEVICE_OFFLINE;

@Service
public class OpenDeviceServiceImpl implements OpenDeviceService {

    @Autowired
    @Qualifier("deviceInfoDataCache")
    private IDeviceInfoData deviceInfoData;

    @Autowired
    @Qualifier("productDataCache")
    private IProductData productData;

    @Autowired
    @Qualifier("thingModelDataCache")
    private IThingModelData thingModelData;

    @Autowired
    private DeviceService deviceService;

    @Override
    public DeviceInfo getDetail(OpenapiDeviceBo data) {
        DeviceInfo deviceInfo = deviceInfoData.findByProductKeyAndDeviceName(data.getProductKey(), data.getDeviceName());
        deviceInfo.setProperty(deviceInfoData.getProperties(deviceInfo.getDeviceId()));
        return deviceInfo;
    }

    @Override
    public boolean addDevice(OpenapiDeviceBo bo) {
        String productKey = bo.getProductKey();
        String deviceName = bo.getDeviceName();
        String parentId = bo.getParentId();

        Product product = productData.findByProductKey(productKey);
        if (product == null) {
            throw new BizException(ErrCode.PRODUCT_NOT_FOUND);
        }
        //同产品不可重复设备名
        DeviceInfo deviceRepetition = deviceInfoData.findByProductKeyAndDeviceName(productKey, deviceName);
        if (deviceRepetition != null) {
            throw new BizException(ErrCode.MODEL_DEVICE_ALREADY);
        }
        //生成设备密钥
        String chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
        int maxPos = chars.length();
        StringBuilder secret = new StringBuilder();
        for (var i = 0; i < 16; i++) {
            secret.append(chars.charAt((int) Math.floor(Math.random() * maxPos)));
        }

        DeviceInfo device = new DeviceInfo();
        device.setId(DeviceUtil.newDeviceId(deviceName));
        device.setUid(product.getUid());
        device.setDeviceId(device.getId());
        device.setProductKey(productKey);
        device.setDeviceName(deviceName);
        device.setSecret(secret.toString());
        device.setState(new DeviceInfo.State(false, null, null));
//        device.setLocate(new DeviceInfo.Locate(deviceInfo.getLongitude(),deviceInfo.getLatitude()));
        device.setCreateAt(System.currentTimeMillis());
        if (StringUtils.isNotBlank(parentId)) {
            device.setParentId(parentId);
        }
        deviceInfoData.save(device);
        return true;
    }

    @Override
    public boolean deleteDevice(OpenapiDeviceBo bo) {
        DeviceInfo deviceRepetition = deviceInfoData.findByProductKeyAndDeviceName(bo.getProductKey(), bo.getDeviceName());
        if (deviceRepetition == null){
            throw new BizException(ErrCode.DEVICE_NOT_FOUND);
        }
        deviceInfoData.deleteById(deviceRepetition.getDeviceId());
        return true;
    }

    @Override
    public String setProperty(String productKey, String deviceName, Map<String, Object> args) {
        DeviceInfo deviceRepetition = deviceInfoData.findByProductKeyAndDeviceName(productKey, deviceName);
        return deviceService.setProperty(deviceRepetition.getDeviceId(), args, true);
    }

    @Override
    public OpenDevicePropertyVo getDevicePropertyStatus(OpenapiDeviceBo bo) {
        ThingModel thingModel = thingModelData.findByProductKey(bo.getProductKey());
        OpenDevicePropertyVo propertyVo = MapstructUtils.convert(thingModel, OpenDevicePropertyVo.class);
        DeviceInfo deviceInfo = deviceInfoData.findByProductKeyAndDeviceName(bo.getProductKey(), bo.getDeviceName());
        if (propertyVo != null){
            propertyVo.setProperty(deviceInfoData.getProperties(deviceInfo.getDeviceId()));
        }
        return propertyVo;
    }


}
