package cc.iotkit.dao;

import cc.iotkit.model.device.VirtualDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VirtualDeviceRepository extends MongoRepository<VirtualDevice, String> {

    Page<VirtualDevice> findByUid(String uid, Pageable pageable);

    List<VirtualDevice> findByUidAndState(String uid, String state);

    List<VirtualDevice> findByTriggerAndState(String trigger, String state);

}
