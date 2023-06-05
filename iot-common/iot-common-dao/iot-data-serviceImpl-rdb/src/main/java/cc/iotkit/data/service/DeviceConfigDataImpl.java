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

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.IDeviceConfigData;
import cc.iotkit.data.dao.DeviceConfigRepository;
import cc.iotkit.data.model.TbDeviceConfig;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.device.DeviceConfig;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Primary
@Service
public class DeviceConfigDataImpl implements IDeviceConfigData, IJPACommData<DeviceConfig, String> {

    @Autowired
    private DeviceConfigRepository deviceConfigRepository;

    @Override
    public DeviceConfig findByProductKeyAndDeviceName(String productKey, String deviceName) {
        return MapstructUtils.convert(deviceConfigRepository.findByProductKeyAndDeviceName(productKey, deviceName), DeviceConfig.class);
    }

    @Override
    public DeviceConfig findByDeviceId(String deviceId) {
        return MapstructUtils.convert(deviceConfigRepository.findByDeviceId(deviceId), DeviceConfig.class);
    }

    @Override
    public JpaRepository getBaseRepository() {
        return deviceConfigRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbDeviceConfig.class;
    }

    @Override
    public Class getTClass() {
        return DeviceConfig.class;
    }


    @Override
    public DeviceConfig save(DeviceConfig data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        deviceConfigRepository.save(MapstructUtils.convert(data, TbDeviceConfig.class));
        return data;
    }






}
