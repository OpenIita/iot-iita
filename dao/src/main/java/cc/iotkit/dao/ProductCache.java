package cc.iotkit.dao;

import cc.iotkit.common.Constants;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.product.ThingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class ProductCache {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ThingModelRepository thingModelRepository;

    @Cacheable(value = Constants.PRODUCT_CACHE, key = "'pk'+#pk", unless = "#result == null")
    public Product findById(String pk) {
        return productRepository.findById(pk).orElse(new Product());
    }

    @Cacheable(value = Constants.THING_MODEL_CACHE, key = "'pk'+#pk", unless = "#result == null")
    public ThingModel getThingModel(String pk) {
        return thingModelRepository.findByProductKey(pk);
    }
}
