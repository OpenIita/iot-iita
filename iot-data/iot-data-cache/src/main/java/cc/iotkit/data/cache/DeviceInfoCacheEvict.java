package cc.iotkit.data.cache;

import cc.iotkit.common.Constants;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
public class DeviceInfoCacheEvict {

    @CacheEvict(value = Constants.CACHE_DEVICE_INFO, key = "#root.method.name+#deviceId")
    public void findByDeviceId(String deviceId) {
    }

    @CacheEvict(value = Constants.CACHE_DEVICE_INFO, key = "#root.method.name+#productKey+#deviceName")
    public void findByProductKeyAndDeviceName(String productKey, String deviceName) {
    }

}
