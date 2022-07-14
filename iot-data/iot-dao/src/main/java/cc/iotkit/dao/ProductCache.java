/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.dao;

import cc.iotkit.common.Constants;
import cc.iotkit.data.IProductModelData;
import cc.iotkit.data.IProductData;
import cc.iotkit.data.IThingModelData;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.product.ProductModel;
import cc.iotkit.model.product.ThingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class ProductCache {

    @Autowired
    private IProductData productData;
    @Autowired
    private IThingModelData thingModelData;
    @Autowired
    private IProductModelData productModelData;

    private static ProductCache INSTANCE;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

    public static ProductCache getInstance() {
        return INSTANCE;
    }

    @Cacheable(value = Constants.PRODUCT_CACHE, key = "'product'+#pk")
    public Product findById(String pk) {
        return productData.findById(pk);
    }

    @Cacheable(value = Constants.THING_MODEL_CACHE, key = "'thing_model'+#pk")
    public ThingModel getThingModel(String pk) {
        return thingModelData.findByProductKey(pk);
    }

    @Cacheable(value = Constants.PRODUCT_SCRIPT_CACHE, key = "'product_script'+#model")
    public ProductModel getProductScriptByModel(String model) {
        return productModelData.findByModel(model);
    }


}
