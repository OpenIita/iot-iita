package cc.iotkit.dao;

import cc.iotkit.model.rule.TaskInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskInfoRepository extends MongoRepository<TaskInfo, String> {
}
