package cc.iotkit.dao;

import cc.iotkit.model.device.DeviceGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceGroupRepository extends MongoRepository<DeviceGroup, String> {

    Page<DeviceGroup> findByNameLike(String name, Pageable pageable);

}
