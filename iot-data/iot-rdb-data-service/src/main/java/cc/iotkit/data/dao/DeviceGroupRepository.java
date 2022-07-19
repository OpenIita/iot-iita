package cc.iotkit.data.dao;

import cc.iotkit.data.model.TbDeviceGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceGroupRepository extends JpaRepository<TbDeviceGroup, String> {

    Page<TbDeviceGroup> findByNameLike(String name, Pageable pageable);

}
