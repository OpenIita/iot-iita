/*
 *
 *  * | Licensed 未经许可不能去掉「OPENIITA」相关版权
 *  * +----------------------------------------------------------------------
 *  * | Author: xw2sy@163.com
 *  * +----------------------------------------------------------------------
 *
 *  Copyright [2024] [OPENIITA]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */

package cc.iotkit.data.service;

import cc.iotkit.common.api.Paging;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.DeviceGroupRepository;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.IDeviceGroupData;
import cc.iotkit.data.model.TbDeviceGroup;
import cc.iotkit.model.device.DeviceGroup;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Primary
@Service
public class DeviceGroupDataImpl implements IDeviceGroupData, IJPACommData<DeviceGroup, String> {

    @Autowired
    private DeviceGroupRepository deviceGroupRepository;

    @Override
    public JpaRepository getBaseRepository() {
        return deviceGroupRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbDeviceGroup.class;
    }

    @Override
    public Class getTClass() {
        return DeviceGroup.class;
    }

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
    public List<DeviceGroup> findAll() {
        return MapstructUtils.convert(deviceGroupRepository.findAll(), DeviceGroup.class);
    }


}
