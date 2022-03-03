package cc.iotkit.dao;

import cc.iotkit.model.rule.SceneInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SceneInfoRepository extends MongoRepository<SceneInfo, String> {
}
