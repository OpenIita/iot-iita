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
import cc.iotkit.data.dao.SpaceRepository;
import cc.iotkit.data.manager.ISpaceData;
import cc.iotkit.data.model.TbSpace;
import cc.iotkit.model.space.Space;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class SpaceDataImpl implements ISpaceData, IJPACommData<Space, Long> {

    @Autowired
    private SpaceRepository spaceRepository;


    @Override
    public JpaRepository getBaseRepository() {
        return spaceRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbSpace.class;
    }

    @Override
    public Class getTClass() {
        return Space.class;
    }

    @Override
    public List<Space> findByHomeId(Long homeId) {
        return MapstructUtils.convert(spaceRepository.findByHomeId(homeId), Space.class);
    }
}
