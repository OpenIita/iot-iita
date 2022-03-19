package cc.iotkit.dao;

import cc.iotkit.model.rule.TaskLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskLogRepository extends MongoRepository<TaskLog, String> {

    void deleteByTaskId(String taskId);

}
