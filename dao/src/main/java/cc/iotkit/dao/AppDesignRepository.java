package cc.iotkit.dao;

import cc.iotkit.model.product.AppDesign;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppDesignRepository extends MongoRepository<AppDesign, String> {
}
