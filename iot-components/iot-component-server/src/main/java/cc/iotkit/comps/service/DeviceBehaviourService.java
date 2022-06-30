package cc.iotkit.comps.service;

import cc.iotkit.common.Constants;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.DeviceUtil;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.common.utils.UniqueIdUtil;
import cc.iotkit.comp.model.DeviceState;
import cc.iotkit.comp.model.RegisterInfo;
import cc.iotkit.dao.*;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.product.ProductModel;
import cc.iotkit.mq.MqProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DeviceBehaviourService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductModelRepository productModelRepository;
    @Autowired
    private ProductCache productCache;
    @Autowired
    private DeviceInfoRepository deviceInfoRepository;
    @Autowired
    private DeviceCache deviceCache;
    @Autowired
    private MqProducer<ThingModelMessage> producer;

    public void register(RegisterInfo info) {
        try {
            DeviceInfo deviceInfo = register(null, info);
            //子设备注册
            List<RegisterInfo.SubDevice> subDevices = info.getSubDevices();
            if (subDevices != null && subDevices.size() != 0) {
                for (RegisterInfo.SubDevice subDevice : subDevices) {
                    register(deviceInfo.getDeviceId(),
                            new RegisterInfo(subDevice.getProductKey(),
                                    subDevice.getDeviceName(),
                                    subDevice.getModel(),
                                    subDevice.getTag(), null));
                }
            }
        } catch (BizException e) {
            log.error("register device error", e);
            throw e;
        } catch (Throwable e) {
            log.error("register device error", e);
            throw new BizException("register device error", e);
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
                ProductModel productModel = productModelRepository.findByModel(model);
                if (productModel == null) {
                    throw new BizException("product model does not exist");
                }
                pk = productModel.getProductKey();
            }
        }

        Optional<Product> optProduct = productRepository.findById(pk);
        if (optProduct.isEmpty()) {
            throw new BizException("Product does not exist");
        }
        Product product = optProduct.get();
        String uid = product.getUid();
        DeviceInfo device = deviceInfoRepository.findByProductKeyAndDeviceName(pk, info.getDeviceName());
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
        deviceInfoRepository.save(device);

        //新设备或更换网关需要产生注册消息
        if (reportMsg) {
            log.info("device registered:{}", JsonUtil.toJsonString(device));
            //新注册设备注册消息
            ThingModelMessage modelMessage = new ThingModelMessage(
                    UniqueIdUtil.newRequestId(), "",
                    pk, dn,
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
        DeviceInfo deviceInfo = deviceInfoRepository.findByProductKeyAndDeviceName(productKey, deviceName);
        if (deviceInfo == null) {
            throw new BizException("device does not exist");
        }
        if (!Constants.PRODUCT_SECRET.equals(productSecret)) {
            throw new BizException("incorrect productSecret");
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

    public void deviceStateChange(String productKey,
                                  String deviceName,
                                  boolean online) {
        DeviceInfo device = deviceInfoRepository.findByProductKeyAndDeviceName(productKey, deviceName);
        if (device == null) {
            log.warn(String.format("productKey: %s,device: %s,online: %s", productKey, device, online));
            throw new BizException("device does not exist");
        }
        deviceStateChange(device, online);

        if (device.getParentId() != null) {
            return;
        }

        List<DeviceInfo> subDevices = deviceInfoRepository.findByParentId(device.getDeviceId());
        for (DeviceInfo subDevice : subDevices) {
            Product product = productCache.findById(subDevice.getProductKey());
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
//            deviceStateHolder.online(device.getDeviceId());
        } else {
            device.getState().setOnline(false);
            device.getState().setOfflineTime(System.currentTimeMillis());
//            deviceStateHolder.offline(device.getDeviceId());
        }
        deviceInfoRepository.save(device);

        //设备状态变更消息
        ThingModelMessage modelMessage = new ThingModelMessage(
                UniqueIdUtil.newRequestId(), "",
                device.getProductKey(), device.getDeviceName(),
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
            DeviceInfo device = deviceCache.getDeviceInfo(message.getProductKey(),
                    message.getDeviceName());
            if (device == null) {
                return;
            }
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
        ThingModelMessage message = JsonUtil.parse(jsonMsg, ThingModelMessage.class);
        reportMessage(message);
    }
}
