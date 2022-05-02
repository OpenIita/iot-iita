package cc.iotkit.dao;

import cc.iotkit.common.Constants;
import cc.iotkit.model.device.DeviceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class DeviceCache {

    @Autowired
    private DeviceRepository deviceRepository;

    private static DeviceCache INSTANCE;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

    public static DeviceCache getInstance() {
        return INSTANCE;
    }

    @Cacheable(value = Constants.DEVICE_CACHE, key = "#pk+'_'+#dn")
    public DeviceInfo getDeviceInfo(String pk, String dn) {
        return deviceRepository.findByProductKeyAndDeviceName(pk, dn);
    }

    @Cacheable(value = Constants.DEVICE_CACHE, key = "#deviceId")
    public DeviceInfo findByDeviceId(String deviceId) {
        return deviceRepository.findByDeviceId(deviceId);
    }

    @Cacheable(value = Constants.DEVICE_CACHE, key = "#deviceId")
    public DeviceInfo get(String deviceId) {
        return deviceRepository.findById(deviceId).orElse(new DeviceInfo());
    }

}
