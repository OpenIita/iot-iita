package cc.iotkit.dao;

import cc.iotkit.model.device.message.DeviceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceEventDao extends BaseDao<DeviceEvent> {

    @Autowired
    public DeviceEventDao(MongoTemplate mongoTemplate) {
        super(mongoTemplate, DeviceEvent.class);
    }
}
