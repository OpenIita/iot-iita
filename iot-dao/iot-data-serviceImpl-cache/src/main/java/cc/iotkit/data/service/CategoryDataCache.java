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
import cc.iotkit.data.cache.CategoryCacheEvict;
import cc.iotkit.data.manager.ICategoryData;
import cc.iotkit.model.product.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@Qualifier("categoryDataCache")
public class CategoryDataCache implements ICategoryData {

    @Autowired
    private ICategoryData categoryData;
    @Autowired
    private CategoryCacheEvict categoryCacheEvict;

    @Override
    @Cacheable(value = Constants.CACHE_CATEGORY, key = "#root.method.name+#s", unless = "#result == null")
    public Category findById(String s) {
        return categoryData.findById(s);
    }

    @Override
    public List<Category> findByIds(Collection<String> id) {
        return Collections.emptyList();
    }

    @Override
    public Category save(Category data) {
        data = categoryData.save(data);
        categoryCacheEvict.findById(data.getId());
        return data;
    }

    @Override
    public void batchSave(List<Category> data) {

    }

    @Override
    public void deleteById(String s) {
        categoryData.deleteById(s);
    }

    @Override
    public void deleteByIds(Collection<String> strings) {

    }

    @Override
    public long count() {
        return categoryData.count();
    }

    @Override
    public List<Category> findAll() {
        return categoryData.findAll();
    }

    @Override
    public Paging<Category> findAll(PageRequest<Category> pageRequest) {
        return categoryData.findAll(pageRequest);
    }

    @Override
    public List<Category> findAllByCondition(Category data) {
        return Collections.emptyList();
    }

    @Override
    public Category findOneByCondition(Category data) {
        return null;
    }

}
