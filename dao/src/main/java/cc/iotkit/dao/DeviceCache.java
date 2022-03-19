package cc.iotkit.dao;

import cc.iotkit.common.Constants;
import cc.iotkit.model.device.DeviceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class DeviceCache extends BaseDao<DeviceInfo> {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    public DeviceCache(MongoTemplate mongoTemplate) {
        super(mongoTemplate, DeviceInfo.class);
    }

    @Cacheable(value = Constants.DEVICE_CACHE, key = "#pk+'_'+#dn")
    public DeviceInfo findByProductKeyAndDeviceName(String pk, String dn) {
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
