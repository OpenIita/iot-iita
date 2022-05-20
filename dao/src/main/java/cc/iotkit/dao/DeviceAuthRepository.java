package cc.iotkit.dao;

import cc.iotkit.model.device.DeviceAuthInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceAuthRepository extends MongoRepository<DeviceAuthInfo, String> {

    DeviceAuthInfo findByProductKeyAndDeviceName(String productKey, String deviceName);

    DeviceAuthInfo findByClientId(String clientId);

    List<DeviceAuthInfo> findByDeviceName(String deviceName);

}
