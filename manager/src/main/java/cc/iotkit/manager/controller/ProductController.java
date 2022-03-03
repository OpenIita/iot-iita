package cc.iotkit.manager.controller;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.dao.CategoryRepository;
import cc.iotkit.dao.ProductRepository;
import cc.iotkit.dao.ThingModelRepository;
import cc.iotkit.manager.config.AliyunConfig;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.model.product.Category;
import cc.iotkit.model.PagingData;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.product.ThingModel;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

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
    private OSS ossClient;

    @PostMapping("/list")
    public PagingData<Product> getProducts(Product form) {
        form = dataOwnerService.wrapExample(form);
        return new PagingData<>(productRepository.count(Example.of(form)),
                productRepository.findAll(Example.of(form)));
    }

    @PostMapping("/save")
    public void save(Product product) {
        product.setId(product.getCode());
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
        productKey = getProduct(productKey).getCode();
        return thingModelRepository.findById(productKey).orElse(new ThingModel(productKey));
    }

    @PostMapping("/thingModel/save")
    public void saveThingModel(String productKey, String model) {
        productKey = getProduct(productKey).getCode();
        thingModelRepository.save(new ThingModel(productKey, productKey, JsonUtil.parse(model, ThingModel.Model.class)));
    }

    @DeleteMapping("/thingModel/{productKey}")
    public void deleteThingModel(String productKey) {
        productKey = getProduct(productKey).getCode();
        thingModelRepository.deleteById(productKey);
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @PreAuthorize("hasAuthority('iot_admin')")
    @PostMapping("/saveCategory")
    public void saveCategory(Category cate) {
        cate.setCreateAt(System.currentTimeMillis());
        categoryRepository.save(cate);
    }

    @PreAuthorize("hasAuthority('iot_admin')")
    @PostMapping("/delCategory")
    public void delCategory(String id) {
        categoryRepository.deleteById(id);
    }

    @SneakyThrows
    @PostMapping("/uploadImg/{productKey}")
    public String uploadImg(@PathVariable("productKey") String productKey,
                            @RequestParam("file") MultipartFile file) {
        productKey = getProduct(productKey).getCode();

        String fileName = file.getOriginalFilename();
        String end = fileName.substring(fileName.lastIndexOf("."));
        if (ossClient == null) {
            // 创建OSSClient实例。
            ossClient = new OSSClientBuilder().build(aliyunConfig.getEndpoint(),
                    aliyunConfig.getAccessKeyId(), aliyunConfig.getAccessKeySecret());
        }

        fileName = "product/" + productKey + "/cover" + end;
        String bucket = "iotkit-img";
        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
        PutObjectResult result = ossClient.putObject(bucket, fileName,
                file.getInputStream());
        return ossClient.generatePresignedUrl(bucket, fileName,
                new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 10)).toString();
    }
}
