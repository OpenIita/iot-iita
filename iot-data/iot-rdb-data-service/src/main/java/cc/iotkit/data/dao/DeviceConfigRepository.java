package cc.iotkit.data.dao;

import cc.iotkit.data.model.TbDeviceConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceConfigRepository extends JpaRepository<TbDeviceConfig, String> {

    TbDeviceConfig findByProductKeyAndDeviceName(String productKey, String deviceName);

    TbDeviceConfig findByDeviceId(String deviceId);

}
