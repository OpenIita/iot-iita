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
import cc.iotkit.data.dao.CategoryRepository;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.ICategoryData;
import cc.iotkit.data.model.TbCategory;
import cc.iotkit.model.product.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Primary
@Service
public class CategoryDataImpl implements ICategoryData, IJPACommData<Category, String> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public JpaRepository getBaseRepository() {
        return categoryRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbCategory.class;
    }

    @Override
    public Class getTClass() {
        return Category.class;
    }

    @Override
    public Category findById(String s) {
        return MapstructUtils.convert(categoryRepository.findById(s).orElse(null), Category.class);
    }

    @Override
    public List<Category> findByIds(Collection<String> id) {
        return Collections.emptyList();
    }

    @Override
    public Category save(Category data) {
        TbCategory tb = categoryRepository.save(MapstructUtils.convert(data, TbCategory.class));
        data.setId(tb.getId());
        return data;
    }





    @Override
    public long count() {
        return categoryRepository.count();
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll().stream()
                .map(c -> MapstructUtils.convert(c, Category.class))
                .collect(Collectors.toList());
    }



}
