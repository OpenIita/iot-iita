package cc.iotkit.dao;

import cc.iotkit.model.device.DeviceInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends MongoRepository<DeviceInfo, String> {

    DeviceInfo findByProductKeyAndDeviceName(String productKey, String deviceName);

    DeviceInfo findByDeviceId(String deviceId);

    List<DeviceInfo> findByParentId(String parentId);

    List<DeviceInfo> findByDeviceName(String deviceName);

}
