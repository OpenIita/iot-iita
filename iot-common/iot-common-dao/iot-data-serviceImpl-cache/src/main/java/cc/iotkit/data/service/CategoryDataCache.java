package cc.iotkit.data.service;

import cc.iotkit.common.constant.Constants;
import cc.iotkit.data.manager.ICategoryData;
import cc.iotkit.data.cache.CategoryCacheEvict;
import cc.iotkit.model.Paging;
import cc.iotkit.model.product.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("categoryDataCache")
public class CategoryDataCache implements ICategoryData {

    @Autowired
    private ICategoryData categoryData;
    @Autowired
    private CategoryCacheEvict categoryCacheEvict;

    @Override
    @Cacheable(value = Constants.CACHE_CATEGORY, key = "#root.method.name+#s", unless = "#result == null")
    public Category findById(String s) {
        return categoryData.findById(s);
    }

    @Override
    public Category save(Category data) {
        data = categoryData.save(data);
        categoryCacheEvict.findById(data.getId());
        return data;
    }

    @Override
    public Category add(Category data) {
        return categoryData.add(data);
    }

    @Override
    public void deleteById(String s) {
        categoryData.deleteById(s);
    }

    @Override
    public void deleteByIds(String[] strings) {

    }

    @Override
    public long count() {
        return categoryData.count();
    }

    @Override
    public List<Category> findAll() {
        return categoryData.findAll();
    }

    @Override
    public Paging<Category> findAll(int page, int size) {
        return categoryData.findAll(page, size);
    }
}
