package cc.iotkit.data.service;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.manager.IProductData;
import cc.iotkit.data.dao.ProductRepository;
import cc.iotkit.data.model.TbProduct;
import cc.iotkit.model.Paging;
import cc.iotkit.model.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class ProductDataImpl implements IProductData {

    @Autowired
    private ProductRepository productRepository;


    @Override
    public List<Product> findByCategory(String category) {
        return MapstructUtils.convert(productRepository.findByCategory(category), Product.class);
    }

    @Override
    public List<Product> findByUid(String uid) {
        return MapstructUtils.convert(productRepository.findByUid(uid), Product.class);
    }

    @Override
    public Paging<Product> findByUid(String uid, int page, int size) {
        Page<TbProduct> productPage = productRepository.findByUid(uid,
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(productPage.getTotalElements(),
                MapstructUtils.convert(productPage.getContent(), Product.class));
    }

    @Override
    public long countByUid(String uid) {
        return productRepository.countByUid(uid);
    }

    @Override
    public Product findById(String s) {
        return MapstructUtils.convert(productRepository.findById(s).orElse(null), Product.class);
    }

    @Override
    public Product save(Product data) {
        productRepository.save(MapstructUtils.convert(data, TbProduct.class));
        return data;
    }

    @Override
    public Product add(Product data) {
        data.setCreateAt(System.currentTimeMillis());
        return save(data);
    }

    @Override
    public void deleteById(String s) {
        productRepository.deleteById(s);
    }

    @Override
    public void deleteByIds(String[] strings) {

    }

    @Override
    public long count() {
        return productRepository.count();
    }

    @Override
    public List<Product> findAll() {
        return MapstructUtils.convert(productRepository.findAll(), Product.class);
    }

    @Override
    public Paging<Product> findAll(int page, int size) {
        Page<TbProduct> productPage = productRepository.findAll(
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(productPage.getTotalElements(),
                MapstructUtils.convert(productPage.getContent(), Product.class));
    }
}
