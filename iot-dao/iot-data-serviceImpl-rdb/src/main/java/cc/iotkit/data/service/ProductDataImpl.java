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

import cc.iotkit.common.api.Paging;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.ProductRepository;
import cc.iotkit.data.manager.IProductData;
import cc.iotkit.data.model.TbProduct;
import cc.iotkit.model.product.Product;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static cc.iotkit.data.model.QTbProduct.tbProduct;

@Primary
@Service
public class ProductDataImpl implements IProductData, IJPACommData<Product, Long> {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public JpaRepository getBaseRepository() {
        return productRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbProduct.class;
    }

    @Override
    public Class getTClass() {
        return Product.class;
    }

    @Override
    public List<Product> findByCategory(String category) {
        return MapstructUtils.convert(productRepository.findByCategory(category), Product.class);
    }

    @Override
    public Product findByProductKey(String productKey) {
        return MapstructUtils.convert(productRepository.findByProductKey(productKey), Product.class);
    }

    @Override
    public void delByProductKey(String productKey) {
        jpaQueryFactory.delete(tbProduct).where(tbProduct.productKey.eq(productKey)).execute();
    }

    public List<Product> findByUid(String uid) {
        return MapstructUtils.convert(productRepository.findByUid(uid), Product.class);
    }

    public Paging<Product> findByUid(String uid, int page, int size) {
        Page<TbProduct> productPage = productRepository.findByUid(uid,
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(productPage.getTotalElements(),
                MapstructUtils.convert(productPage.getContent(), Product.class));
    }

    public long countByUid(String uid) {
        return productRepository.countByUid(uid);
    }

    @Override
    public Product save(Product data) {
        productRepository.save(MapstructUtils.convert(data, TbProduct.class));
        return data;
    }

}
