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

package cc.iotkit.manager.service;

import cc.iotkit.common.api.Paging;
import cc.iotkit.common.thing.ThingModelMessage;
import cc.iotkit.model.space.SpaceDevice;

import java.util.List;

/**
 * @Author：tfd
 * @Date：2023/8/25 14:46
 */
public interface ISpaceDeviceService {

    List<SpaceDevice> findByHomeIdAndCollect(Long homeId,boolean collect);

    SpaceDevice findByDeviceId(String deviceId);

    SpaceDevice save(SpaceDevice spaceDevice);

    List<SpaceDevice> findByHomeId(Long homeId);

    List<SpaceDevice> findBySpaceId(Long spaceId);

    void deleteById (Long id);

    SpaceDevice findById (Long id);

    Paging<ThingModelMessage> findByTypeAndDeviceIds(List<String> deviceIds, String type,
                                                     String identifier,
                                                     int page, int size);
}
