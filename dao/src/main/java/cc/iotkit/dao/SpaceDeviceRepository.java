package cc.iotkit.dao;

import cc.iotkit.model.space.SpaceDevice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceDeviceRepository extends MongoRepository<SpaceDevice, String> {

    List<SpaceDevice> findByUidOrderByUseAtDesc(String uid);

    List<SpaceDevice> findByUidAndSpaceIdOrderByAddAtDesc(String uid, String spaceId);

}
