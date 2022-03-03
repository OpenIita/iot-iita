package cc.iotkit.server.dao;

import cc.iotkit.model.device.message.DeviceEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceEventRepository extends MongoRepository<DeviceEvent, String> {

    Page<DeviceEvent> findByDeviceId(String deviceId, Pageable pageable);
}
