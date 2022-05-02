package cc.iotkit.dao;

import cc.iotkit.model.product.ProductModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductModelRepository extends MongoRepository<ProductModel, String> {

    ProductModel findByModel(String model);

    List<ProductModel> findByProductKey(String productKey);

}
