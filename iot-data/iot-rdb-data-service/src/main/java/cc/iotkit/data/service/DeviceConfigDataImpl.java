/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.data.service;

import cc.iotkit.data.IDeviceConfigData;
import cc.iotkit.data.dao.DeviceConfigRepository;
import cc.iotkit.data.convert.DeviceConfigMapper;
import cc.iotkit.data.model.TbDeviceConfig;
import cc.iotkit.model.Paging;
import cc.iotkit.model.device.DeviceConfig;
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
public class DeviceConfigDataImpl implements IDeviceConfigData {

    @Autowired
    private DeviceConfigRepository deviceConfigRepository;

    @Override
    public DeviceConfig findByProductKeyAndDeviceName(String productKey, String deviceName) {
        return DeviceConfigMapper.M.toDto(deviceConfigRepository.findByProductKeyAndDeviceName(productKey, deviceName));
    }

    @Override
    public DeviceConfig findByDeviceId(String deviceId) {
        return DeviceConfigMapper.M.toDto(deviceConfigRepository.findByDeviceId(deviceId));
    }

    @Override
    public DeviceConfig findById(String s) {
        return DeviceConfigMapper.M.toDto(deviceConfigRepository.findById(s).orElse(null));
    }

    @Override
    public DeviceConfig save(DeviceConfig data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        deviceConfigRepository.save(DeviceConfigMapper.M.toVo(data));
        return data;
    }

    @Override
    public DeviceConfig add(DeviceConfig data) {
        return save(data);
    }

    @Override
    public void deleteById(String s) {
        deviceConfigRepository.deleteById(s);
    }

    @Override
    public long count() {
        return deviceConfigRepository.count();
    }

    @Override
    public List<DeviceConfig> findAll() {
        return deviceConfigRepository.findAll().stream().map(DeviceConfigMapper.M::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Paging<DeviceConfig> findAll(int page, int size) {
        Page<TbDeviceConfig> tbDeviceConfigs = deviceConfigRepository.findAll(Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(
                tbDeviceConfigs.getTotalElements(),
                tbDeviceConfigs.getContent()
                        .stream().map(DeviceConfigMapper.M::toDto)
                        .collect(Collectors.toList())
        );
    }
}
