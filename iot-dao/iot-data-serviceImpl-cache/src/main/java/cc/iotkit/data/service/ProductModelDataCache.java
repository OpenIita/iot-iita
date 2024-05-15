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
import cc.iotkit.data.manager.IProductModelData;
import cc.iotkit.data.cache.ProductModelCacheEvict;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.product.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Qualifier("productModelDataCache")
public class ProductModelDataCache implements IProductModelData {

    @Autowired
    private IProductModelData productModelData;
    @Autowired
    private ProductModelCacheEvict productModelCacheEvict;

    @Override
    @Cacheable(value = Constants.CACHE_PRODUCT_SCRIPT, key = "#root.method.name+#model", unless = "#result == null")
    public ProductModel findByModel(String model) {
        return productModelData.findByModel(model);
    }

    @Override
    public List<ProductModel> findByProductKey(String productKey) {
        return productModelData.findByProductKey(productKey);
    }

    @Override
    public ProductModel findById(String s) {
        return productModelData.findById(s);
    }

    @Override
    public List<ProductModel> findByIds(Collection<String> id) {
        return null;
    }

    @Override
    public ProductModel save(ProductModel data) {
        ProductModel productModel = productModelData.save(data);
        productModelCacheEvict.findByModel(data.getModel());
        return productModel;
    }

    @Override
    public void batchSave(List<ProductModel> data) {

    }

    @Override
    public void deleteById(String s) {
        productModelData.deleteById(s);
    }

    @Override
    public void deleteByIds(Collection<String> strings) {

    }

    @Override
    public long count() {
        return productModelData.count();
    }

    @Override
    public List<ProductModel> findAll() {
        return productModelData.findAll();
    }

    @Override
    public Paging<ProductModel> findAll(PageRequest<ProductModel> pageRequest) {
        return productModelData.findAll(pageRequest);
    }

    @Override
    public List<ProductModel> findAllByCondition(ProductModel data) {
        return null;
    }

    @Override
    public ProductModel findOneByCondition(ProductModel data) {
        return null;
    }

}
