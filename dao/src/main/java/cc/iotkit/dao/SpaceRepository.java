package cc.iotkit.dao;

import cc.iotkit.model.space.Space;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceRepository extends MongoRepository<Space, String> {
}
