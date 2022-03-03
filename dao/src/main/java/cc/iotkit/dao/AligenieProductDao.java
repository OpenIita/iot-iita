package cc.iotkit.dao;

import cc.iotkit.model.aligenie.AligenieProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AligenieProductDao extends BaseDao<AligenieProduct> {

    private final AligenieProductRepository aligenieProductRepository;

    @Autowired
    public AligenieProductDao(MongoTemplate mongoTemplate,
                              AligenieProductRepository aligenieProductRepository) {
        super(mongoTemplate, AligenieProduct.class);
        this.aligenieProductRepository = aligenieProductRepository;
    }

    @Cacheable(value = "cache_getAligenieProduct", key = "'getAligenieProduct'+#pk", unless = "#result == null")
    public AligenieProduct getAligenieProduct(String pk) {
        return aligenieProductRepository.findOne(Example.of(
                AligenieProduct.builder().productKey(pk).build()
        )).orElse(null);
    }
}
