package cc.iotkit.data.dao;

import cc.iotkit.data.model.TbDeviceTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceTagRepository extends JpaRepository<TbDeviceTag, String> {

    List<TbDeviceTag> findByDeviceId(String deviceId);

    TbDeviceTag findByDeviceIdAndCode(String deviceId, String code);

}
