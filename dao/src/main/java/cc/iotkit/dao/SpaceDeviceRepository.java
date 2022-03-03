package cc.iotkit.dao;

import cc.iotkit.model.space.SpaceDevice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceDeviceRepository extends MongoRepository<SpaceDevice, String> {
}
