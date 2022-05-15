package cc.iotkit.dao;

import cc.iotkit.common.Constants;
import cc.iotkit.model.product.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class CategoryCache {

    @Autowired
    private CategoryRepository categoryRepository;

    private static CategoryCache INSTANCE;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

    public static CategoryCache getInstance() {
        return INSTANCE;
    }

    @Cacheable(value = Constants.CATEGORY_CACHE, key = "#id")
    public Category getById(String id) {
        return categoryRepository.findById(id).orElse(null);
    }
}
