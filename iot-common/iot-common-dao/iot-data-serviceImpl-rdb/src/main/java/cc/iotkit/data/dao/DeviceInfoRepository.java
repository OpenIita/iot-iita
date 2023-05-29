package cc.iotkit.data.dao;

import cc.iotkit.data.model.TbDeviceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceInfoRepository extends JpaRepository<TbDeviceInfo, String> {

    TbDeviceInfo findByDeviceId(String deviceId);

    TbDeviceInfo findByProductKeyAndDeviceName(String productKey, String deviceName);

    List<TbDeviceInfo> findByParentId(String parentId);

    List<TbDeviceInfo> findByDeviceName(String deviceName);

}
