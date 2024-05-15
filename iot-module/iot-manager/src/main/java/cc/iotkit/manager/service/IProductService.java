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

package cc.iotkit.manager.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.manager.dto.bo.category.CategoryBo;
import cc.iotkit.manager.dto.bo.product.IconBo;
import cc.iotkit.manager.dto.bo.product.IconTypeBo;
import cc.iotkit.manager.dto.bo.product.ProductBo;
import cc.iotkit.manager.dto.bo.productmodel.ProductModelBo;
import cc.iotkit.manager.dto.bo.thingmodel.ThingModelBo;
import cc.iotkit.manager.dto.vo.category.CategoryVo;
import cc.iotkit.manager.dto.vo.product.IconTypeVo;
import cc.iotkit.manager.dto.vo.product.IconVo;
import cc.iotkit.manager.dto.vo.product.ProductVo;
import cc.iotkit.manager.dto.vo.productmodel.ProductModelVo;
import cc.iotkit.manager.dto.vo.thingmodel.ThingModelVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: jay
 * @Date: 2023/5/30 16:23
 * @Version: V1.0
 * @Description: 产品服务接口
 */
public interface IProductService {
    ProductVo addEntity(ProductBo data);

    boolean updateEntity(ProductBo productBo);

    ProductVo getDetail(String data);

    boolean deleteProduct(String productKey);

    ThingModelVo getThingModelByProductKey(String data);

    boolean saveThingModel(ThingModelBo data);

    boolean deleteThingModel(Long id);

    boolean deleteCategory(String data);

    boolean editCategory(CategoryBo req);

    String uploadImg(String productKey, MultipartFile file);

    Paging<ProductVo> selectPageList(PageRequest<ProductBo> request);

    Paging<CategoryVo> selectCategoryPageList(PageRequest<CategoryBo> request);

    List<CategoryVo> selectCategoryList();

    List<ProductModelVo> getModels(String productKey);

    boolean editProductModel(ProductModelBo productModel);

    boolean deleteProductModel(String id);

    ProductVo findByProductKey(String productKey);

    boolean saveIconType(IconTypeBo data);

    boolean deleteIconType(Long data);

    boolean saveIcon(IconBo data);

    boolean deleteIcon(Long data);

    List<IconTypeVo> selectIconTypeList();

    Paging<IconVo> selectIconPageList(PageRequest<IconBo> request);
}
