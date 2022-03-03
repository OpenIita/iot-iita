package cc.iotkit.mqttauth.dao;

import cc.iotkit.model.device.DeviceInfo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class DeviceDao {

    @Resource
    private MongoTemplate mongoTemplate;

    public void addDevice(DeviceInfo device) {
        device.setId(device.getDeviceId());
        device.setCreateAt(System.currentTimeMillis());
        mongoTemplate.insert(device);
    }

    public void updateDevice(DeviceInfo device) {
        if (device.getDeviceId() == null) {
            return;
        }
        mongoTemplate.updateFirst(query(where("deviceId").is(device.getDeviceId())),
                DaoTool.update(device), DeviceInfo.class);
    }

    public void updateDeviceByPkAndDn(DeviceInfo device) {
        if (device.getProductKey() == null || device.getDeviceName() == null) {
            return;
        }
        mongoTemplate.updateFirst(query(where("productKey").is(device.getProductKey()).
                        and("deviceName").is(device.getDeviceName())),
                DaoTool.update(device), DeviceInfo.class);
    }

    @Cacheable(value = "deviceInfoCache", key = "#pk+'_'+#dn")
    public DeviceInfo getByPkAndDn(String pk, String dn) {
        Query query = query(where("productKey").is(pk).and("deviceName").is(dn));
        return mongoTemplate.findOne(query, DeviceInfo.class);
    }

    public DeviceInfo getByDeviceId(String deviceId) {
        Query query = query(where("deviceId").is(deviceId));
        return mongoTemplate.findOne(query, DeviceInfo.class);
    }
}
