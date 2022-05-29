package cc.iotkit.dao;

import cc.iotkit.model.product.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    int countBy();


}
