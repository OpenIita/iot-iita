package cc.iotkit.data.service;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.manager.IProductModelData;
import cc.iotkit.data.dao.ProductModelRepository;
import cc.iotkit.data.model.TbProductModel;
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
        return MapstructUtils.convert(productModelRepository.findByModel(model), ProductModel.class);
    }

    @Override
    public List<ProductModel> findByProductKey(String productKey) {
        return MapstructUtils.convert(productModelRepository.findByProductKey(productKey), ProductModel.class);
    }

    @Override
    public ProductModel findById(String s) {
        return MapstructUtils.convert(productModelRepository.findById(s).orElse(null), ProductModel.class);
    }

    @Override
    public ProductModel save(ProductModel data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        data.setModifyAt(System.currentTimeMillis());
        productModelRepository.save(MapstructUtils.convert(data, TbProductModel.class));
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
    public void deleteByIds(String[] strings) {

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
