package cc.iotkit.protocol.server.service;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.dao.DeviceRepository;
import cc.iotkit.dao.ProductRepository;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.product.Product;
import cc.iotkit.protocol.RegisterInfo;
import cc.iotkit.protocol.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class DeviceBehaviourService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DeviceRepository deviceRepository;


    public Result register(RegisterInfo info) {
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
            //todo 产生设备注册事件
        } catch (BizException e) {
            log.error("register device error", e);
            return new Result(false, e.getMessage());
        } catch (Throwable e) {
            log.error("register device error", e);
            return new Result(false, "unknown error:" + e.getMessage());
        }

        return new Result(true, "");
    }

    public DeviceInfo register(String parentId, RegisterInfo info) {
        String pk = info.getProductKey();
        Optional<Product> optProduct = productRepository.findById(pk);
        if (!optProduct.isPresent()) {
            throw new BizException("Product does not exist");
        }
        String uid = optProduct.get().getUid();
        DeviceInfo device = deviceRepository.findByProductKeyAndDeviceName(pk, info.getDeviceName());

        if (device != null) {
            //更新设备信息
            device.setParentId(parentId);
            device.setUid(uid);
            Map<String, Object> tag = info.getTag();
            Map<String, Object> oldTag = device.getTag();

            if (oldTag == null) {
                oldTag = new HashMap<>();
            }

            if (tag != null) {
                oldTag.putAll(tag);
            }

            device.setTag(oldTag);
        } else {
            //不存在,注册新设备
            device = new DeviceInfo();
            device.setParentId(parentId);
            device.setUid(uid);
            device.setDeviceId(newDeviceId(info.getDeviceName()));
            device.setProductKey(info.getProductKey());
            device.setDeviceName(info.getDeviceName());
            device.setTag(info.getTag());
            device.setState(new DeviceInfo.State(false, null, null));
            device.setCreateAt(System.currentTimeMillis());
        }

        deviceRepository.save(device);
        log.info("device registered:{}", JsonUtil.toJsonString(device));

        return device;
    }

    /**
     * 1-13位	时间戳
     * 14-29位	deviceNae，去除非字母和数字，不足16位补0，超过16位的mac取后16位，共16位
     * 30-31位	mac长度，共2位
     * 32位	随机一个0-f字符
     */
    public static String newDeviceId(String deviceNae) {
        int maxDnLen = 16;
        String dn = deviceNae.replaceAll("[^0-9A-Za-z]", "");
        if (dn.length() > maxDnLen) {
            dn = dn.substring(dn.length() - maxDnLen);
        } else {
            dn = (dn + "00000000000000000000").substring(0, maxDnLen);
        }
        String len = StringUtils.leftPad(deviceNae.length() + "", 2, '0');
        String rnd = Integer.toHexString(RandomUtils.nextInt(0, 16));
        return (System.currentTimeMillis() + "0" + dn + len + rnd).toLowerCase();
    }

    public Result deviceStateChange(String productKey,
                                    String deviceName,
                                    boolean online) {
        DeviceInfo device = deviceRepository.findByProductKeyAndDeviceName(productKey, deviceName);
        if (device == null) {
            return new Result(false, "device does not exist");
        }

        if (online) {
            device.getState().setOnline(true);
            device.getState().setOnlineTime(System.currentTimeMillis());
        } else {
            device.getState().setOnline(false);
            device.getState().setOfflineTime(System.currentTimeMillis());
        }
        deviceRepository.save(device);
        //todo 产生在离线事件

        return new Result(true, "");
    }

}
