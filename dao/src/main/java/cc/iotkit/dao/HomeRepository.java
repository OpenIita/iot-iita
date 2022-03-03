package cc.iotkit.dao;

import cc.iotkit.model.space.Home;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeRepository extends MongoRepository<Home, String> {
}
