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

package cc.iotkit.manager.service.impl;

import cc.iotkit.common.api.Paging;
import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.thing.ThingModelMessage;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.manager.ISpaceDeviceData;
import cc.iotkit.manager.service.ISpaceDeviceService;
import cc.iotkit.model.space.SpaceDevice;
import cc.iotkit.temporal.IThingModelMessageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author：tfd
 * @Date：2023/8/25 14:46
 */
@Service
public class SpaceDeviceServiceImpl implements ISpaceDeviceService {

    @Autowired
    private ISpaceDeviceData spaceDeviceData;

    @Autowired
    private IThingModelMessageData thingModelMessageData;

    @Override
    public List<SpaceDevice> findByHomeIdAndCollect(Long homeId, boolean collect) {
        return spaceDeviceData.findByHomeIdAndCollect(homeId,collect);
    }

    @Override
    public SpaceDevice findByDeviceId(String deviceId) {
        return spaceDeviceData.findByDeviceId(deviceId);
    }

    @Override
    public SpaceDevice save(SpaceDevice spaceDevice) {
        return spaceDeviceData.save(spaceDevice);
    }

    @Override
    public List<SpaceDevice> findByHomeId(Long homeId) {
        return spaceDeviceData.findByHomeId(homeId);
    }

    @Override
    public List<SpaceDevice> findBySpaceId(Long spaceId) {
        return spaceDeviceData.findBySpaceId(spaceId);
    }

    @Override
    public void deleteById(Long id) {
        spaceDeviceData.deleteById(id);
    }

    @Override
    public SpaceDevice findById(Long id) {
        return spaceDeviceData.findById(id);
    }

    @Override
    public Paging<ThingModelMessage> findByTypeAndDeviceIds(List<String> deviceIds, String type, String identifier, int page, int size) {
        if(StringUtils.isEmpty(type)){
            throw new BizException(ErrCode.PARAMS_EXCEPTION);
        }
        return thingModelMessageData.findByTypeAndDeviceIds(deviceIds,type,identifier,page,size);
    }
}
