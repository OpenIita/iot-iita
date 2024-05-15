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
import cc.iotkit.data.dao.ProductModelRepository;
import cc.iotkit.data.manager.IProductModelData;
import cc.iotkit.data.model.TbProductModel;
import cc.iotkit.model.product.ProductModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Primary
@Service
public class ProductModelDataImpl implements IProductModelData, IJPACommData<ProductModel, String> {

    @Autowired
    private ProductModelRepository productModelRepository;


    @Override
    public JpaRepository getBaseRepository() {
        return productModelRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbProductModel.class;
    }

    @Override
    public Class getTClass() {
        return ProductModel.class;
    }

    @Override
    public ProductModel findByModel(String model) {
        return MapstructUtils.convert(productModelRepository.findByModel(model), ProductModel.class);
    }

    @Override
    public List<ProductModel> findByProductKey(String productKey) {
        return MapstructUtils.convert(productModelRepository.findByProductKey(productKey), ProductModel.class);
    }

    @Override
    public ProductModel findById(String s) {
        return MapstructUtils.convert(productModelRepository.findById(s).orElse(null), ProductModel.class);
    }

    @Override
    public List<ProductModel> findByIds(Collection<String> id) {
        return Collections.emptyList();
    }

    @Override
    public ProductModel save(ProductModel data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        data.setModifyAt(System.currentTimeMillis());
        productModelRepository.save(MapstructUtils.convert(data, TbProductModel.class));
        return null;
    }



}
