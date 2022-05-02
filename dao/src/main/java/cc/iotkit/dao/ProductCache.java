package cc.iotkit.dao;

import cc.iotkit.common.Constants;
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
    private ProductRepository productRepository;
    @Autowired
    private ThingModelRepository thingModelRepository;
    @Autowired
    private ProductModelRepository productModelRepository;

    private static ProductCache INSTANCE;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

    public static ProductCache getInstance() {
        return INSTANCE;
    }

    @Cacheable(value = Constants.PRODUCT_CACHE, key = "'pk'+#pk", unless = "#result == null")
    public Product findById(String pk) {
        return productRepository.findById(pk).orElse(new Product());
    }

    @Cacheable(value = Constants.THING_MODEL_CACHE, key = "'pk'+#pk", unless = "#result == null")
    public ThingModel getThingModel(String pk) {
        return thingModelRepository.findByProductKey(pk);
    }

    @Cacheable(value = Constants.PRODUCT_SCRIPT_CACHE, key = "'pk'+#pk", unless = "#result == null")
    public ProductModel getProductScript(String pk) {
        return productModelRepository.findById(pk).orElse(null);
    }

    @Cacheable(value = Constants.PRODUCT_SCRIPT_CACHE, key = "'model'+#model", unless = "#result == null")
    public ProductModel getProductScriptByModel(String model) {
        return productModelRepository.findByModel(model);
    }


}
