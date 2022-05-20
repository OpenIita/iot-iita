package cc.iotkit.dao;

import cc.iotkit.model.device.DeviceAclInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceAclRepository extends MongoRepository<DeviceAclInfo, String> {

    DeviceAclInfo findByProductKeyAndDeviceName(String productKey, String deviceName);

    DeviceAclInfo findByClientId(String clientId);

    List<DeviceAclInfo> findByDeviceName(String deviceName);

}
