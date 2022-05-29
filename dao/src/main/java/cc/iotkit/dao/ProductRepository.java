package cc.iotkit.dao;

import cc.iotkit.model.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    long countByUid(String uid);

    List<Product> findByUid(String uid);
}
