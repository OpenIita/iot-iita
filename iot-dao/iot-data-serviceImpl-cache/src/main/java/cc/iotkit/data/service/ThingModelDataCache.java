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
import cc.iotkit.common.constant.Constants;
import cc.iotkit.data.manager.IThingModelData;
import cc.iotkit.data.cache.ThingModelCacheEvict;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.product.ThingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Qualifier("thingModelDataCache")
public class ThingModelDataCache implements IThingModelData {

    @Autowired
    private IThingModelData thingModelData;
    @Autowired
    private ThingModelCacheEvict thingModelCacheEvict;

    @Override
    @Cacheable(value = Constants.CACHE_THING_MODEL, key = "#root.method.name+#s", unless = "#result == null")
    public ThingModel findById(Long s) {
        return thingModelData.findById(s);
    }

    @Override
    public List<ThingModel> findByIds(Collection<Long> id) {
        return null;
    }

    @Override
    public ThingModel save(ThingModel data) {
        data = thingModelData.save(data);
        thingModelCacheEvict.findById(data.getId());
        thingModelCacheEvict.findByProductKey(data.getProductKey());
        return data;
    }

    @Override
    public void batchSave(List<ThingModel> data) {

    }

    @Override
    public void deleteById(Long s) {
        thingModelData.deleteById(s);
        thingModelCacheEvict.findById(s);
    }

    @Override
    public void deleteByIds(Collection<Long> ids) {

    }

    @Override
    public long count() {
        return thingModelData.count();
    }

    @Override
    public List<ThingModel> findAll() {
        return thingModelData.findAll();
    }

    @Override
    public Paging<ThingModel> findAll(PageRequest<ThingModel> pageRequest) {
        return thingModelData.findAll(pageRequest);
    }

    @Override
    public List<ThingModel> findAllByCondition(ThingModel data) {
        return null;
    }

    @Override
    public ThingModel findOneByCondition(ThingModel data) {
        return null;
    }

    @Override
    @Cacheable(value = Constants.CACHE_THING_MODEL, key = "#root.method.name+#productKey", unless = "#result == null")
    public ThingModel findByProductKey(String productKey) {
        return thingModelData.findByProductKey(productKey);
    }
}
