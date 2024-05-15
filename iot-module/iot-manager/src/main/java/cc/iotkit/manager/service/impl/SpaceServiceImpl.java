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

import cc.iotkit.data.manager.ISpaceData;
import cc.iotkit.manager.service.ISpaceService;
import cc.iotkit.model.space.Space;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author：tfd
 * @Date：2023/8/25 10:23
 */
@Service
public class SpaceServiceImpl implements ISpaceService {
    @Autowired
    private ISpaceData spaceData;

    @Override
    public Space save(Space space) {
        return spaceData.save(space);
    }

    @Override
    public List<Space> findByHomeId(Long homeId) {
        return spaceData.findByHomeId(homeId);
    }

    @Override
    public Space findById(Long id) {
        return spaceData.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        spaceData.deleteById(id);
    }
}
