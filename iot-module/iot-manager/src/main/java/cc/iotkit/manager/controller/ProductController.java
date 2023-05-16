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

import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.data.manager.ICategoryData;
import cc.iotkit.data.manager.IProductData;
import cc.iotkit.data.manager.IProductModelData;
import cc.iotkit.data.manager.IThingModelData;
import cc.iotkit.manager.config.AliyunConfig;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.model.Paging;
import cc.iotkit.model.product.Category;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.product.ProductModel;
import cc.iotkit.model.product.ThingModel;
import cc.iotkit.temporal.IDbStructureData;
import cc.iotkit.utils.AuthUtil;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Api(tags = {"产品"})
@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    @Qualifier("productDataCache")
    private IProductData productData;
    @Autowired
    @Qualifier("thingModelDataCache")
    private IThingModelData thingModelData;
    @Autowired
    @Qualifier("categoryDataCache")
    private ICategoryData categoryData;
    @Autowired
    private DataOwnerService dataOwnerService;
    @Autowired
    private AliyunConfig aliyunConfig;
    @Autowired
    @Qualifier("productModelDataCache")
    private IProductModelData productModelData;
    @Autowired
    private IDbStructureData dbStructureData;


    private OSS ossClient;

    @PostMapping("/list/{size}/{page}")
    public Paging<Product> getProducts(
            @PathVariable("size") int size,
            @PathVariable("page") int page) {
        if (!AuthUtil.isAdmin()) {
            return productData.findByUid(AuthUtil.getUserId(), page, size);
        }

        return productData.findAll(page, size);
    }

    @PostMapping("/save")
    public void save(Product product) {
        dataOwnerService.checkOwnerSave(productData, product);

        if (product.getCreateAt() == null) {
            product.setCreateAt(System.currentTimeMillis());
        }
        productData.save(product);
    }

    @GetMapping("/{productKey}")
    public Product getProduct(@PathVariable("productKey") String productKey) {
        return dataOwnerService.checkOwner(productData.findById(productKey));
    }

    @GetMapping("/thingModel/{productKey}")
    public ThingModel getThingModel(@PathVariable("productKey") String productKey) {
        checkProductOwner(productKey);
        return thingModelData.findById(productKey);
    }

    @PostMapping("/thingModel/save")
    public void saveThingModel(String productKey, String model) {
        checkProductOwner(productKey);
        ThingModel oldData = thingModelData.findById(productKey);
        ThingModel thingModel = new ThingModel(productKey, productKey, JsonUtil.parse(model, ThingModel.Model.class));
        if (oldData == null) {
            //定义时序数据库物模型数据结构
            dbStructureData.defineThingModel(thingModel);
        } else {
            //更新时序数据库物模型数据结构
            dbStructureData.updateThingModel(thingModel);
        }
        thingModelData.save(thingModel);
    }

    @PostMapping("/thingModel/{productKey}/delete")
    public void deleteThingModel(String productKey) {
        checkProductOwner(productKey);
        ThingModel thingModel = thingModelData.findById(productKey);
        //删除时序数据库物模型数据结构
        dbStructureData.defineThingModel(thingModel);
        thingModelData.deleteById(productKey);
    }

    private void checkProductOwner(String productKey) {
        dataOwnerService.checkOwner(productData.findById(productKey));
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryData.findAll();
    }

    @SaCheckRole("iot_admin")
    @PostMapping("/saveCategory")
    public void saveCategory(Category cate) {
        cate.setCreateAt(System.currentTimeMillis());
        categoryData.save(cate);
    }

    @SaCheckRole("iot_admin")
    @PostMapping("/delCategory")
    public void delCategory(String id) {
        categoryData.deleteById(id);
    }

    @SneakyThrows
    @PostMapping("/uploadImg/{productKey}")
    public String uploadImg(@PathVariable("productKey") String productKey,
                            @RequestParam("file") MultipartFile file) {
        productKey = getProduct(productKey).getId();

        String fileName = file.getOriginalFilename();
        String end = fileName.substring(fileName.lastIndexOf("."));
        if (ossClient == null) {
            // 创建OSSClient实例。
            ossClient = new OSSClientBuilder().build(aliyunConfig.getEndpoint(),
                    aliyunConfig.getAccessKeyId(), aliyunConfig.getAccessKeySecret());
        }

        fileName = "product/" + productKey + "/cover" + end;
        String bucket = aliyunConfig.getBucketId();
        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
        PutObjectResult result = ossClient.putObject(bucket, fileName,
                file.getInputStream());
        return ossClient.generatePresignedUrl(bucket, fileName,
                new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 10)).toString();
    }

    @GetMapping("/{productKey}/models")
    public List<ProductModel> getModels(@PathVariable("productKey") String productKey) {
        dataOwnerService.checkOwner(productData, productKey);
        return productModelData.findByProductKey(productKey);
    }

    @PostMapping("/saveProductModel")
    public void saveProductModel(ProductModel productModel) {
        String model = productModel.getModel();
        String productKey = productModel.getProductKey();
        Product product = productData.findById(productKey);
        if (product == null) {
            throw new BizException(ErrCode.PRODUCT_NOT_FOUND);
        }
        dataOwnerService.checkOwner(product);

        ProductModel oldScript = productModelData.findByModel(model);
        if (oldScript != null && !oldScript.getProductKey().equals(productKey)) {
            throw new BizException(ErrCode.MODEL_ALREADY);
        }

        productModel.setModifyAt(System.currentTimeMillis());
        productModelData.save(productModel);
    }
}
