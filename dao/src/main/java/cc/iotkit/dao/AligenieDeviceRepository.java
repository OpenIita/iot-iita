package cc.iotkit.dao;

import cc.iotkit.model.aligenie.AligenieDevice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AligenieDeviceRepository extends MongoRepository<AligenieDevice, String> {
}
