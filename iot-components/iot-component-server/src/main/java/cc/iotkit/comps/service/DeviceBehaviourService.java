/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comps.service;

import cc.iotkit.common.constant.Constants;
import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.DeviceUtil;
import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.common.utils.UniqueIdUtil;
import cc.iotkit.comp.model.DeviceState;
import cc.iotkit.comp.model.RegisterInfo;
import cc.iotkit.data.manager.IDeviceInfoData;
import cc.iotkit.data.manager.IProductData;
import cc.iotkit.data.manager.IProductModelData;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.product.ProductModel;
import cc.iotkit.mq.MqProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class DeviceBehaviourService {

    @Autowired
    private IProductModelData productModelData;
    @Autowired
    @Qualifier("deviceInfoDataCache")
    private IDeviceInfoData deviceInfoData;
    @Autowired
    private MqProducer<ThingModelMessage> producer;
    @Autowired
    @Qualifier("productDataCache")
    private IProductData productData;

    public void register(RegisterInfo info) {
        try {
            DeviceInfo deviceInfo = register(null, info);
            //子设备注册
            List<RegisterInfo.SubDevice> subDevices = info.getSubDevices();
            if (subDevices == null) {
                return;
            }
            for (RegisterInfo.SubDevice subDevice : subDevices) {
                register(deviceInfo.getDeviceId(),
                        new RegisterInfo(subDevice.getProductKey(),
                                subDevice.getDeviceName(),
                                subDevice.getModel(),
                                subDevice.getTag(), null));
            }
        } catch (BizException e) {
            log.error("register device error", e);
            throw e;
        } catch (Throwable e) {
            log.error("register device error", e);
            throw new BizException(ErrCode.DEVICE_REGISTER_ERROR, e);
        }
    }

    public DeviceInfo register(String parentId, RegisterInfo info) {
        String pk = info.getProductKey();
        String dn = info.getDeviceName();
        String model = info.getModel();

        //子设备注册处理
        if (parentId != null) {
            //透传设备：pk为空、model不为空，使用model查询产品
            if (StringUtils.isBlank(pk) && StringUtils.isNotBlank(model)) {
                ProductModel productModel = productModelData.findByModel(model);
                if (productModel == null) {
                    throw new BizException(ErrCode.PRODUCT_MODEL_NOT_FOUND);
                }
                pk = productModel.getProductKey();
            }
        }

        Product product = productData.findById(pk);
        if (product == null) {
            throw new BizException(ErrCode.PRODUCT_NOT_FOUND);
        }
        String uid = product.getUid();
        DeviceInfo device = deviceInfoData.findByProductKeyAndDeviceName(pk, info.getDeviceName());
        boolean reportMsg = false;

        if (device != null) {
            log.info("device already registered");
            device.setModel(model);
        } else {
            //不存在,注册新设备
            device = new DeviceInfo();
            device.setId(DeviceUtil.newDeviceId(dn));
            device.setParentId(parentId);
            device.setUid(uid);
            device.setDeviceId(device.getId());
            device.setProductKey(pk);
            device.setDeviceName(dn);
            device.setModel(model);
            //默认离线
            device.setState(new DeviceInfo.State(false, null, null));
            device.setCreateAt(System.currentTimeMillis());
            reportMsg = true;


            //auth、acl

        }

        //透传设备，默认在线
        if (product.isTransparent()) {
            device.setState(new DeviceInfo.State(true, System.currentTimeMillis(), null));
        }

        if (parentId != null) {
            //子设备更换网关重新注册更新父级ID
            device.setParentId(parentId);
            reportMsg = true;
        }
        deviceInfoData.save(device);

        //新设备或更换网关需要产生注册消息
        if (reportMsg) {
            log.info("device registered:{}", JsonUtils.toJsonString(device));
            //新注册设备注册消息
            ThingModelMessage modelMessage = new ThingModelMessage(
                    UUID.randomUUID().toString(),
                    UniqueIdUtil.newRequestId(), "",
                    pk, dn, uid,
                    ThingModelMessage.TYPE_LIFETIME, "register",
                    0, new HashMap<>(), System.currentTimeMillis(),
                    System.currentTimeMillis()
            );

            reportMessage(modelMessage);
        }

        return device;
    }

    public void deviceAuth(String productKey,
                           String deviceName,
                           String productSecret,
                           String deviceSecret) {
        DeviceInfo deviceInfo = deviceInfoData.findByProductKeyAndDeviceName(productKey, deviceName);
        if (deviceInfo == null) {
            throw new BizException(ErrCode.DEVICE_NOT_FOUND);
        }
        if (!Constants.PRODUCT_SECRET.equals(productSecret)) {
            throw new BizException(ErrCode.PRODUCT_SECRET_ERROR);
        }

        //todo 按产品ProductSecret认证，子设备需要父设备认证后可通过验证
//        Optional<Product> optProduct = productRepository.findById(productKey);
//        if (!optProduct.isPresent()) {
//            throw new BizException("product does not exist");
//        }
//        Product product = optProduct.get();
//        if (product.getNodeType()) {
//
//        }

    }

    public boolean isOnline(String productKey,
                            String deviceName) {
        DeviceInfo device = deviceInfoData.findByProductKeyAndDeviceName(productKey, deviceName);
        DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(device.getDeviceId());
        return deviceInfo.getState().isOnline();
    }

    public void deviceStateChange(String productKey,
                                  String deviceName,
                                  boolean online) {
        DeviceInfo device = deviceInfoData.findByProductKeyAndDeviceName(productKey, deviceName);
        if (device == null) {
            log.warn("productKey: {},deviceName:{},online: {}", productKey, deviceName, online);
            throw new BizException(ErrCode.DEVICE_NOT_FOUND);
        }
        deviceStateChange(device, online);

        //父设备ID不为空说明是子设备
        if (device.getParentId() != null) {
            return;
        }

        //否则为父设备，同步透传子设备状态
        List<String> subDeviceIds = deviceInfoData.findSubDeviceIds(device.getDeviceId());
        for (String subDeviceId : subDeviceIds) {
            DeviceInfo subDevice = deviceInfoData.findByDeviceId(subDeviceId);
            Product product = productData.findById(subDevice.getProductKey());
            Boolean transparent = product.getTransparent();
            //透传设备父设备上线，子设备也上线。非透传设备父设备离线，子设备才离线
            if (transparent != null && transparent || !online) {
                deviceStateChange(subDevice, online);
            }
        }
    }

    private void deviceStateChange(DeviceInfo device, boolean online) {
        if (online) {
            device.getState().setOnline(true);
            device.getState().setOnlineTime(System.currentTimeMillis());
        } else {
            device.getState().setOnline(false);
            device.getState().setOfflineTime(System.currentTimeMillis());
        }
        deviceInfoData.save(device);

        //设备状态变更消息
        ThingModelMessage modelMessage = new ThingModelMessage(
                UUID.randomUUID().toString(),
                UniqueIdUtil.newRequestId(), "",
                device.getProductKey(), device.getDeviceName(), device.getUid(),
                ThingModelMessage.TYPE_STATE,
                online ? DeviceState.STATE_ONLINE : DeviceState.STATE_OFFLINE,
                0,
                new HashMap<>(), System.currentTimeMillis(),
                System.currentTimeMillis()
        );

        reportMessage(modelMessage);
    }

    public void reportMessage(ThingModelMessage message) {
        try {
            DeviceInfo device = deviceInfoData.findByProductKeyAndDeviceName(
                    message.getProductKey(), message.getDeviceName());
            if (device == null) {
                return;
            }
            message.setId(UUID.randomUUID().toString());
            if (message.getOccurred() == null) {
                message.setOccurred(System.currentTimeMillis());
            }
            if (message.getTime() == null) {
                message.setTime(System.currentTimeMillis());
            }
            message.setDeviceId(device.getDeviceId());

            producer.publish(Constants.THING_MODEL_MESSAGE_TOPIC, message);

        } catch (Throwable e) {
            log.error("send thing model message error", e);
        }
    }

    /**
     * 提供给js调用的方法
     */
    public void reportMessage(String jsonMsg) {
        ThingModelMessage message = JsonUtils.parseObject(jsonMsg, ThingModelMessage.class);
        reportMessage(message);
    }
}
