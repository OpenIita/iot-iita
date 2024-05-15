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
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.SpaceDeviceRepository;
import cc.iotkit.data.manager.ISpaceDeviceData;
import cc.iotkit.data.model.TbSpaceDevice;
import cc.iotkit.model.space.SpaceDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class SpaceDeviceDataImpl implements ISpaceDeviceData, IJPACommData<SpaceDevice, Long> {

    @Autowired
    private SpaceDeviceRepository spaceDeviceRepository;


    @Override
    public JpaRepository getBaseRepository() {
        return spaceDeviceRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbSpaceDevice.class;
    }

    @Override
    public Class getTClass() {
        return SpaceDevice.class;
    }


    @Override
    public List<SpaceDevice> findByHomeIdAndCollect(Long homeId, boolean collect) {
        return MapstructUtils.convert(spaceDeviceRepository.findByHomeIdAndCollect(homeId, collect), SpaceDevice.class);
    }

    @Override
    public List<SpaceDevice> findByHomeId(Long homeId) {
        return MapstructUtils.convert(spaceDeviceRepository.findByHomeId(homeId), SpaceDevice.class);
    }

    @Override
    public List<SpaceDevice> findBySpaceId(Long spaceId) {
        return MapstructUtils.convert(spaceDeviceRepository.findBySpaceId(spaceId), SpaceDevice.class);
    }

    @Override
    public SpaceDevice findByDeviceId(String deviceId) {
        return MapstructUtils.convert(spaceDeviceRepository.findByDeviceId(deviceId), SpaceDevice.class);
    }

    @Override
    public void deleteAllBySpaceId(Long spaceId) {
        spaceDeviceRepository.deleteAllBySpaceId(spaceId);
    }

}
