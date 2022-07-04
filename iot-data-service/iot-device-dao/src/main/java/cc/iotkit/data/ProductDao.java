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
public interface ProductDao {

    /**
     * 通过产品id取产品信息
     */
    Product findById(String id);

    /**
     * 添加产品
     */
    void add(Product product);

    /**
     * 按id更新产品
     */
    void updateById(Product product);

    /**
     * 按用户id统计产品数量
     */
    long countByUid(String uid);

    /**
     * 按用户id取产品列表
     */
    List<Product> findByUid(String uid);

    /**
     * 按品类取产品列表
     */
    List<Product> findByCategory(String category);

}
