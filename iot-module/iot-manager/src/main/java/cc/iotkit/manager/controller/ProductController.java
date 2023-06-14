/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.controller;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.api.Request;
import cc.iotkit.common.log.annotation.Log;
import cc.iotkit.common.log.enums.BusinessType;
import cc.iotkit.common.validate.AddGroup;
import cc.iotkit.common.validate.EditGroup;
import cc.iotkit.manager.dto.bo.category.CategoryBo;
import cc.iotkit.manager.dto.bo.product.ProductBo;
import cc.iotkit.manager.dto.bo.productmodel.ProductModelBo;
import cc.iotkit.manager.dto.bo.thingmodel.ThingModelBo;
import cc.iotkit.manager.dto.vo.category.CategoryVo;
import cc.iotkit.manager.dto.vo.product.ProductVo;
import cc.iotkit.manager.dto.vo.productmodel.ProductModelVo;
import cc.iotkit.manager.dto.vo.thingmodel.ThingModelVo;
import cc.iotkit.manager.service.IProductService;
import cn.dev33.satoken.annotation.SaCheckRole;
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

    @PostMapping("/list")
    @ApiOperation("列表")
    public Paging<ProductVo> getProducts(@Validated @RequestBody
            PageRequest<ProductBo> request) {
        return productService.selectPageList(request);
    }

    @ApiOperation("新建")
    @PostMapping(value = "/add")
    @Log(title = "产品", businessType = BusinessType.INSERT)
    public ProductVo create(@Validated(AddGroup.class) @RequestBody Request<ProductBo> request) {

        return productService.addEntity(request.getData());

    }

    @ApiOperation(value = "编辑产品")
    @PostMapping("/edit")
    @Log(title = "产品", businessType = BusinessType.UPDATE)
    public boolean edit(@Validated(EditGroup.class) @RequestBody Request<ProductBo> request) {
        return productService.updateEntity(request.getData());
    }


    @ApiOperation("查看详情")
    @PostMapping(value = "/getDetail")
    public ProductVo getDetail(@RequestBody  @Validated Request<String> request) {
        ProductVo dto = productService.getDetail(request.getData());
        return dto;
    }
    @PostMapping("/getThingModelByProductKey")
    @ApiOperation("查看物模型")
    public ThingModelVo getThingModelByProductKey(@RequestBody  @Validated Request<String> request) {

        return productService.getThingModelByProductKey(request.getData());

    }

    @ApiOperation("保存物模型")
    @PostMapping("/thingModel/save")
    public boolean saveThingModel(@Validated @RequestBody Request<ThingModelBo> request) {

        return productService.saveThingModel(request.getData());


    }

    @PostMapping("/thingModel/delete")
    @ApiOperation("删除物模型")
    @Log(title = "物模型", businessType = BusinessType.DELETE)
    public boolean deleteThingModel(@Validated @RequestBody Request<String> productKey) {
       return productService.deleteThingModel(productKey.getData());
    }


    @PostMapping("/category/list")
    @ApiOperation("产品品类分页展示")
    public Paging<CategoryVo> getCategories(@Validated @RequestBody PageRequest<CategoryBo> request) {
        return productService.selectCategoryPageList(request);
    }

    @PostMapping("/category/getList")
    @ApiOperation("产品品类展示")
    public List<CategoryVo> getCategorieList() {
        return productService.selectCategoryList();
    }

    @SaCheckRole("iot_admin")
    @ApiOperation("品类编辑")
    @PostMapping("/category/edit")
    public boolean saveCategory(@Validated @RequestBody Request<CategoryBo> req) {

        return productService.editCategory(req.getData());
    }

    @SaCheckRole("iot_admin")
    @PostMapping("/category/delete")
    @ApiOperation("删除品类")
    public boolean delCategory(@Validated @RequestBody Request<String> req) {
        return productService.deleteCategory(req.getData());

    }

    @ApiOperation("上传产品图片")
    @PostMapping("/uploadImg/{productKey}")
    public String uploadImg(@PathVariable("productKey") String productKey,
                            @RequestParam("file") MultipartFile file) {
       return productService.uploadImg(productKey,file);
    }

    @PostMapping("/getModelsByProductKey")
    @ApiModelProperty("获取产品型号")
    public List<ProductModelVo> getModels(@Validated @RequestBody Request<String> bo) {
        return productService.getModels(bo.getData());
    }

    @PostMapping("/productModel/edit")
    @ApiOperation("编辑产品型号")
    public boolean saveProductModel(@Validated @RequestBody Request<ProductModelBo> bo) {
        return productService.editProductModel(bo.getData());
    }
}
