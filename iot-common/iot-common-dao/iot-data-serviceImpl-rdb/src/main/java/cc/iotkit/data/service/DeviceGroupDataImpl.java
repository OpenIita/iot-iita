package cc.iotkit.data.service;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.manager.IDeviceGroupData;
import cc.iotkit.data.dao.DeviceGroupRepository;
import cc.iotkit.data.model.TbDeviceGroup;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.device.DeviceGroup;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Primary
@Service
public class DeviceGroupDataImpl implements IDeviceGroupData {

    @Autowired
    private DeviceGroupRepository deviceGroupRepository;

    @Override
    public Paging<DeviceGroup> findByNameLike(String name, int page, int size) {
        Page<TbDeviceGroup> groups = deviceGroupRepository.findByNameLike("%" + name.trim() + "%",
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(groups.getTotalElements(),
                MapstructUtils.convert(groups.getContent(), DeviceGroup.class));
    }

    @Override
    public DeviceGroup findById(String s) {
        return MapstructUtils.convert(deviceGroupRepository.findById(s).orElse(null), DeviceGroup.class);
    }

    @Override
    public DeviceGroup save(DeviceGroup data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        deviceGroupRepository.save(MapstructUtils.convert(data, TbDeviceGroup.class));
        return data;
    }


    @Override
    public void deleteById(String s) {
        deviceGroupRepository.deleteById(s);
    }

    @Override
    public void deleteByIds(String[] strings) {

    }

    @Override
    public long count() {
        return deviceGroupRepository.count();
    }

    @Override
    public List<DeviceGroup> findAll() {
        return MapstructUtils.convert(deviceGroupRepository.findAll(), DeviceGroup.class);
    }

    @Override
    public Paging<DeviceGroup> findAll(int page, int size) {
        Page<TbDeviceGroup> groups = deviceGroupRepository.findAll(Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(groups.getTotalElements(), MapstructUtils.convert(groups.getContent(), DeviceGroup.class));
    }
}
