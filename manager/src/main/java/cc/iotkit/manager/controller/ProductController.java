package cc.iotkit.manager.controller;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.dao.CategoryRepository;
import cc.iotkit.dao.ProductRepository;
import cc.iotkit.dao.ProductModelRepository;
import cc.iotkit.dao.ThingModelRepository;
import cc.iotkit.manager.config.AliyunConfig;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.model.Paging;
import cc.iotkit.model.product.Category;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.product.ProductModel;
import cc.iotkit.model.product.ThingModel;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ThingModelRepository thingModelRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private DataOwnerService dataOwnerService;
    @Autowired
    private AliyunConfig aliyunConfig;
    @Autowired
    private ProductModelRepository productModelRepository;

    private OSS ossClient;

    @PostMapping("/list/{size}/{page}")
    public Paging<Product> getProducts(
            @PathVariable("size") int size,
            @PathVariable("page") int page,
            Product form) {
        form = dataOwnerService.wrapExample(form);
        Page<Product> products = productRepository.findAll(Example.of(form),
                PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("createAt")))
        );
        return new Paging<>(products.getTotalElements(), products.getContent());
    }

    @PostMapping("/save")
    public void save(Product product) {
        dataOwnerService.checkOwnerSave(productRepository, product);

        if (product.getCreateAt() == null) {
            product.setCreateAt(System.currentTimeMillis());
        }
        productRepository.save(product);
    }

    @GetMapping("/{productKey}")
    public Product getProduct(@PathVariable("productKey") String productKey) {
        return dataOwnerService.checkOwner(productRepository.findById(productKey).orElse(new Product()));
    }

    @GetMapping("/thingModel/{productKey}")
    public ThingModel getThingModel(@PathVariable("productKey") String productKey) {
        productKey = getProduct(productKey).getId();
        return thingModelRepository.findById(productKey).orElse(new ThingModel(productKey));
    }

    @PostMapping("/thingModel/save")
    public void saveThingModel(String productKey, String model) {
        productKey = getProduct(productKey).getId();
        thingModelRepository.save(new ThingModel(productKey, productKey, JsonUtil.parse(model, ThingModel.Model.class)));
    }

    @PostMapping("/thingModel/{productKey}/delete")
    public void deleteThingModel(String productKey) {
        productKey = getProduct(productKey).getId();
        thingModelRepository.deleteById(productKey);
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @SaCheckRole("iot_admin")
    @PostMapping("/saveCategory")
    public void saveCategory(Category cate) {
        cate.setCreateAt(System.currentTimeMillis());
        categoryRepository.save(cate);
    }

    @SaCheckRole("iot_admin")
    @PostMapping("/delCategory")
    public void delCategory(String id) {
        categoryRepository.deleteById(id);
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
        dataOwnerService.checkOwner(productRepository, productKey);
        return productModelRepository.findByProductKey(productKey);
    }

    @PostMapping("/saveProductModel")
    public void saveProductModel(ProductModel productModel) {
        String model = productModel.getModel();
        String productKey = productModel.getProductKey();
        Optional<Product> optProduct = productRepository.findById(productKey);
        if (!optProduct.isPresent()) {
            throw new BizException("product does not exist");
        }
        dataOwnerService.checkOwner(optProduct.get());

        ProductModel oldScript = productModelRepository.findByModel(model);
        if (oldScript != null && !oldScript.getProductKey().equals(productKey)) {
            throw new BizException("model already exists");
        }

        productModel.setModifyAt(System.currentTimeMillis());
        productModelRepository.save(productModel);
    }
}
