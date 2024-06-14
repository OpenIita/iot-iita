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
package cc.iotkit.manager.controller;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.api.Request;
import cc.iotkit.common.log.annotation.Log;
import cc.iotkit.common.log.enums.BusinessType;
import cc.iotkit.common.validate.AddGroup;
import cc.iotkit.common.validate.EditGroup;
import cc.iotkit.common.validate.QueryGroup;
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
import cc.iotkit.manager.service.IProductService;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = {"产品"})
@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {


    @Autowired
    private IProductService productService;

    @ApiOperation("列表")
    @SaCheckPermission("iot:product:query")
    @PostMapping("/list")
    public Paging<ProductVo> getProducts(@Validated(QueryGroup.class) @RequestBody
                                         PageRequest<ProductBo> request) {
        return productService.selectPageList(request);
    }

    @ApiOperation("新建")
    @SaCheckPermission("iot:product:add")
    @PostMapping(value = "/add")
    @Log(title = "产品", businessType = BusinessType.INSERT)
    public ProductVo create(@Validated(AddGroup.class) @RequestBody Request<ProductBo> request) {
        return productService.addEntity(request.getData());
    }

    @ApiOperation(value = "编辑产品")
    @SaCheckPermission("iot:product:edit")
    @PostMapping("/edit")
    @Log(title = "产品", businessType = BusinessType.UPDATE)
    public boolean edit(@Validated(EditGroup.class) @RequestBody Request<ProductBo> request) {
        return productService.updateEntity(request.getData());
    }

    @ApiOperation("保存图标分类")
    @SaCheckPermission("iot:iconType:edit")
    @PostMapping("/icon/saveIconType")
    public boolean saveIconType(@Validated @RequestBody Request<IconTypeBo> request) {
        return productService.saveIconType(request.getData());
    }

    @ApiOperation("删除图标分类")
    @SaCheckPermission("iot:iconType:remove")
    @PostMapping("/icon/deleteIconType")
    public boolean deleteIconType(@Validated @RequestBody Request<Long> request) {
        return productService.deleteIconType(request.getData());
    }

    @ApiOperation("图标分类列表")
    @SaCheckPermission("iot:iconType:query")
    @PostMapping("/icon/getAllIconType")
    public List<IconTypeVo> getAllIconType() {
        return productService.selectIconTypeList();
    }

    @ApiOperation("保存图标")
    @SaCheckPermission("iot:icon:edit")
    @PostMapping("/icon/saveIcon")
    public boolean saveIcon(@Validated @RequestBody Request<IconBo> request) {
        return productService.saveIcon(request.getData());
    }

    @ApiOperation("删除图标")
    @SaCheckPermission("iot:icon:remove")
    @PostMapping("/icon/deleteIcon")
    public boolean deleteIcon(@Validated @RequestBody Request<Long> request) {
        return productService.deleteIcon(request.getData());
    }

    @ApiOperation("图标分页展示")
    @SaCheckPermission("iot:icon:query")
    @PostMapping("/icon/getAllIcon")
    public Paging<IconVo> getAllIcon(@Validated @RequestBody PageRequest<IconBo> request) {
        return productService.selectIconPageList(request);
    }

    @ApiOperation("查看详情")
    @SaCheckPermission("iot:product:query")
    @PostMapping(value = "/getDetail")
    public ProductVo getDetail(@RequestBody @Validated Request<String> request) {
        return productService.getDetail(request.getData());
    }

    @ApiOperation("删除产品")
    @SaCheckPermission("iot:product:remove")
    @PostMapping(value = "/deleteProduct")
    public boolean deleteProduct(@RequestBody @Validated Request<String> request) {
        return productService.deleteProduct(request.getData());
    }

    @ApiOperation("查看物模型")
    @SaCheckPermission("iot:thingModel:query")
    @PostMapping("/getThingModelByProductKey")
    public ThingModelVo getThingModelByProductKey(@RequestBody @Validated Request<String> request) {
        return productService.getThingModelByProductKey(request.getData());
    }

    @ApiOperation("保存物模型")
    @SaCheckPermission("iot:thingModel:edit")
    @PostMapping("/thingModel/save")
    public boolean saveThingModel(@Validated @RequestBody Request<ThingModelBo> request) {
        return productService.saveThingModel(request.getData());
    }

    @ApiOperation("删除物模型")
    @SaCheckPermission("iot:thingModel:remove")
    @PostMapping("/thingModel/delete")
    @Log(title = "物模型", businessType = BusinessType.DELETE)
    public boolean deleteThingModel(@Validated @RequestBody Request<Long> id) {
        return productService.deleteThingModel(id.getData());
    }

    @ApiOperation("产品品类分页展示")
    @SaCheckPermission("iot:category:query")
    @PostMapping("/category/list")
    public Paging<CategoryVo> getCategories(@Validated @RequestBody PageRequest<CategoryBo> request) {
        return productService.selectCategoryPageList(request);
    }

    @ApiOperation("产品品类展示")
    @SaCheckPermission("iot:category:query")
    @PostMapping("/category/getAll")
    public List<CategoryVo> getCategorieList() {
        return productService.selectCategoryList();
    }

    @ApiOperation("品类编辑")
    @SaCheckPermission("iot:category:edit")
    @PostMapping("/category/edit")
    public boolean saveCategory(@Validated @RequestBody Request<CategoryBo> req) {
        return productService.editCategory(req.getData());
    }

    @ApiOperation("删除品类")
    @SaCheckPermission("iot:category:remove")
    @PostMapping("/category/delete")
    public boolean delCategory(@Validated @RequestBody Request<String> req) {
        return productService.deleteCategory(req.getData());
    }

    @ApiOperation("上传产品图片")
    @SaCheckPermission("iot:product:edit")
    @PostMapping("/uploadImg/{productKey}")
    public String uploadImg(@PathVariable("productKey") String productKey,
                            @RequestParam("file") MultipartFile file) {
        return productService.uploadImg(productKey, file);
    }

    @ApiModelProperty("获取产品型号")
    @SaCheckPermission("iot:product:query")
    @PostMapping("/getModelsByProductKey")
    public List<ProductModelVo> getModels(@Validated @RequestBody Request<String> bo) {
        return productService.getModels(bo.getData());
    }

    @ApiOperation("编辑产品型号")
    @SaCheckPermission("iot:product:edit")
    @PostMapping("/productModel/edit")
    public boolean saveProductModel(@Validated @RequestBody Request<ProductModelBo> bo) {
        return productService.editProductModel(bo.getData());
    }

    @ApiOperation("删除产品型号")
    @SaCheckPermission("iot:product:remove")
    @PostMapping("/productModel/delete")
    public boolean deleteProductModel(@Validated @RequestBody Request<String> id) {
        return productService.deleteProductModel(id.getData());
    }
}
