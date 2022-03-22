package cc.iotkit.dao;

import cc.iotkit.model.aligenie.AligenieProduct;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AligenieProductRepository extends MongoRepository<AligenieProduct, String> {

    List<AligenieProduct> findByUid(String uid);

    AligenieProduct findByProductKey(String productKey);
}
