package cc.iotkit.data.service;

import cc.iotkit.data.manager.IProductModelData;
import cc.iotkit.data.dao.ProductModelRepository;
import cc.iotkit.data.convert.ProductModelMapper;
import cc.iotkit.model.Paging;
import cc.iotkit.model.product.ProductModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Primary
@Service
public class ProductModelDataImpl implements IProductModelData {

    @Autowired
    private ProductModelRepository productModelRepository;

    @Override
    public ProductModel findByModel(String model) {
        return ProductModelMapper.M.toDto(productModelRepository.findByModel(model));
    }

    @Override
    public List<ProductModel> findByProductKey(String productKey) {
        return ProductModelMapper.toDto(productModelRepository.findByProductKey(productKey));
    }

    @Override
    public ProductModel findById(String s) {
        return ProductModelMapper.M.toDto(productModelRepository.findById(s).orElse(null));
    }

    @Override
    public ProductModel save(ProductModel data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        data.setModifyAt(System.currentTimeMillis());
        productModelRepository.save(ProductModelMapper.M.toVo(data));
        return null;
    }

    @Override
    public ProductModel add(ProductModel data) {
        return save(data);
    }

    @Override
    public void deleteById(String s) {
        productModelRepository.deleteById(s);
    }

    @Override
    public long count() {
        return productModelRepository.count();
    }

    @Override
    public List<ProductModel> findAll() {
        return new ArrayList<>();
    }

    @Override
    public Paging<ProductModel> findAll(int page, int size) {
        return new Paging<>();
    }
}
