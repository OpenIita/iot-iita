/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.data.manager;

import cc.iotkit.model.product.ProductModel;

import java.util.List;

public interface IProductModelData extends ICommonData<ProductModel, String> {

    ProductModel findByModel(String model);

    List<ProductModel> findByProductKey(String productKey);

}
