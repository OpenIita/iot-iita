package cc.iotkit.dao;

import cc.iotkit.model.rule.SceneLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SceneLogRepository extends MongoRepository<SceneLog, String> {
}
