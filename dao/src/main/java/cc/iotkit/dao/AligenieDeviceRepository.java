package cc.iotkit.dao;

import cc.iotkit.model.aligenie.AligenieDevice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AligenieDeviceRepository extends MongoRepository<AligenieDevice, String> {

    void deleteByUid(String uid);

    List<AligenieDevice> findByUid(String uid);

    AligenieDevice findByUidAndDeviceId(String uid, String deviceId);

}
