/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.data;

import cc.iotkit.model.product.Product;

import java.util.List;

/**
 * 产品接口
 */
public interface IProductData extends IOwnedData<Product, String> {

    /**
     * 按品类取产品列表
     */
    List<Product> findByCategory(String category);

}
