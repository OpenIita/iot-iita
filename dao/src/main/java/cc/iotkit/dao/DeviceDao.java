package cc.iotkit.dao;

import cc.iotkit.model.Paging;
import cc.iotkit.model.device.DeviceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class DeviceDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Paging<DeviceInfo> find(Criteria condition, int size, int page) {
        Query query = Query.query(condition);
        return new Paging<>(
                mongoTemplate.count(query, DeviceInfo.class),
                mongoTemplate.find(
                        query.with(PageRequest.of(page-1, size, Sort.by(Sort.Order.desc("createAt"))))
                        , DeviceInfo.class)
        );
    }

    /**
     * 更新设备属性
     */
    public void updateProperties(String deviceId, Map<String, Object> properties) {
        Query query = Query.query(new Criteria().and("deviceId").is(deviceId));
        Update update = new Update();
        for (String key : properties.keySet()) {
            update.set("property." + key, properties.get(key));
        }
        mongoTemplate.updateFirst(query, update, DeviceInfo.class);
    }

}
