package cc.iotkit.data.cache;

import cc.iotkit.common.Constants;
import cc.iotkit.model.device.DeviceInfo;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeviceInfoCachePut {

    @CachePut(value = Constants.CACHE_DEVICE_INFO, key = "#root.method.name+#deviceId")
    public DeviceInfo findByDeviceId(String deviceId, DeviceInfo deviceInfo) {
        return deviceInfo;
    }

    @CachePut(value = Constants.CACHE_DEVICE_INFO, key = "#root.method.name+#productKey+#deviceName")
    public DeviceInfo findByProductKeyAndDeviceName(String productKey, String deviceName, DeviceInfo deviceInfo) {
        return deviceInfo;
    }

    @CachePut(value = Constants.CACHE_DEVICE_INFO, key = "#root.method.name+#parentId")
    public List<String> findSubDeviceIds(String parentId, List<String> subDeviceIds) {
        return subDeviceIds;
    }

}
