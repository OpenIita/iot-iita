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

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.constant.Constants;
import cc.iotkit.data.cache.SpaceCacheEvict;
import cc.iotkit.data.manager.ISpaceData;
import cc.iotkit.model.space.Space;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Qualifier("spaceDataCache")
public class SpaceDataCache implements ISpaceData {

    @Autowired
    private ISpaceData spaceData;
    @Autowired
    private SpaceCacheEvict spaceCacheEvict;

    @Override
    public List<Space> findByHomeId(Long homeId) {
        return spaceData.findByHomeId(homeId);
    }

    @Override
    @Cacheable(value = Constants.CACHE_SPACE, key = "#root.method.name+#s", unless = "#result == null")
    public Space findById(Long s) {
        return spaceData.findById(s);
    }

    @Override
    public List<Space> findByIds(Collection<Long> id) {
        return null;
    }

    @Override
    public Space save(Space data) {
        data = spaceData.save(data);
        spaceCacheEvict.findById(data.getId());
        return data;
    }

    @Override
    public void batchSave(List<Space> data) {

    }

    @Override
    public void deleteById(Long s) {
        spaceData.deleteById(s);
    }

    @Override
    public void deleteByIds(Collection<Long> strings) {

    }

    @Override
    public long count() {
        return spaceData.count();
    }

    @Override
    public List<Space> findAll() {
        return spaceData.findAll();
    }

    @Override
    public Paging<Space> findAll(PageRequest<Space> pageRequest) {
        return spaceData.findAll(pageRequest);
    }

    @Override
    public List<Space> findAllByCondition(Space data) {
        return null;
    }

    @Override
    public Space findOneByCondition(Space data) {
        return null;
    }

}
