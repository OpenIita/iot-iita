package cc.iotkit.data.service;

import cc.iotkit.common.constant.Constants;
import cc.iotkit.data.manager.IProductData;
import cc.iotkit.data.cache.ProductCacheEvict;
import cc.iotkit.model.Paging;
import cc.iotkit.model.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("productDataCache")
public class ProductDataCache implements IProductData {

    @Autowired
    private IProductData productData;
    @Autowired
    private ProductCacheEvict productCacheEvict;

    @Override
    public List<Product> findByCategory(String category) {
        return productData.findByCategory(category);
    }

    @Override
    public List<Product> findByUid(String uid) {
        return productData.findByUid(uid);
    }

    @Override
    public Paging<Product> findByUid(String uid, int page, int size) {
        return productData.findByUid(uid, page, size);
    }

    @Override
    public long countByUid(String uid) {
        return productData.countByUid(uid);
    }

    @Override
    @Cacheable(value = Constants.CACHE_PRODUCT, key = "#root.method.name+#s", unless = "#result == null")
    public Product findById(String s) {
        return productData.findById(s);
    }

    @Override
    public Product save(Product data) {
        Product p = productData.save(data);
        //清除缓存
        productCacheEvict.findById(p.getId());
        return p;
    }

    @Override
    public Product add(Product data) {
        return productData.add(data);
    }

    @Override
    public void deleteById(String s) {
        productData.deleteById(s);
        //清除缓存
        productCacheEvict.findById(s);
    }

    @Override
    public long count() {
        return productData.count();
    }

    @Override
    public List<Product> findAll() {
        return productData.findAll();
    }

    @Override
    public Paging<Product> findAll(int page, int size) {
        return productData.findAll(page, size);
    }
}
