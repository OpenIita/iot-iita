package cc.iotkit.dao;

import cc.iotkit.model.product.ThingModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThingModelRepository extends MongoRepository<ThingModel, String> {

    ThingModel findByProductKey(String productKey);

}
