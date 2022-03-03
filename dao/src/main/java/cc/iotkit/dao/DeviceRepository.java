package cc.iotkit.dao;

import cc.iotkit.model.device.DeviceInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends MongoRepository<DeviceInfo, String> {
}
