package cc.iotkit.dao;

import cc.iotkit.model.aligenie.AligenieProduct;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AligenieProductRepository extends MongoRepository<AligenieProduct, String> {
}
