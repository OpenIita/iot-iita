package cc.iotkit.data.service;

import cc.iotkit.data.IDeviceGroupData;
import cc.iotkit.data.dao.DeviceGroupRepository;
import cc.iotkit.data.model.DeviceGroupMapper;
import cc.iotkit.data.model.TbDeviceGroup;
import cc.iotkit.model.Paging;
import cc.iotkit.model.device.DeviceGroup;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Service
public class DeviceGroupDataImpl implements IDeviceGroupData {

    @Autowired
    private DeviceGroupRepository deviceGroupRepository;

    @Override
    public Paging<DeviceGroup> findByNameLike(String name, int page, int size) {
        Page<TbDeviceGroup> groups = deviceGroupRepository.findByNameLike("%" + name.trim() + "%",
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(groups.getTotalElements(), groups.getContent()
                .stream().map(DeviceGroupMapper.M::toDto)
                .collect(Collectors.toList()));
    }

    @Override
    public DeviceGroup findById(String s) {
        return DeviceGroupMapper.M.toDto(deviceGroupRepository.findById(s).orElse(null));
    }

    @Override
    public DeviceGroup save(DeviceGroup data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        deviceGroupRepository.save(DeviceGroupMapper.M.toVo(data));
        return data;
    }

    @Override
    public DeviceGroup add(DeviceGroup data) {
        data.setCreateAt(System.currentTimeMillis());
        return save(data);
    }

    @Override
    public void deleteById(String s) {
        deviceGroupRepository.deleteById(s);
    }

    @Override
    public long count() {
        return deviceGroupRepository.count();
    }

    @Override
    public List<DeviceGroup> findAll() {
        return deviceGroupRepository.findAll()
                .stream().map(DeviceGroupMapper.M::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Paging<DeviceGroup> findAll(int page, int size) {
        Page<TbDeviceGroup> groups = deviceGroupRepository.findAll(Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(groups.getTotalElements(), groups.getContent()
                .stream().map(DeviceGroupMapper.M::toDto)
                .collect(Collectors.toList()));
    }
}
