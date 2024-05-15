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

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.DeviceConfigRepository;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.IDeviceConfigData;
import cc.iotkit.data.model.TbDeviceConfig;
import cc.iotkit.model.device.DeviceConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Primary
@Service
public class DeviceConfigDataImpl implements IDeviceConfigData, IJPACommData<DeviceConfig, String> {

    @Autowired
    private DeviceConfigRepository deviceConfigRepository;

    @Override
    public DeviceConfig findByDeviceName(String deviceName) {
        return MapstructUtils.convert(deviceConfigRepository.findByDeviceName(deviceName), DeviceConfig.class);
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
