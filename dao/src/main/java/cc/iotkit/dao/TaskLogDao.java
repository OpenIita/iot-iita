package cc.iotkit.dao;

import cc.iotkit.model.rule.TaskLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class TaskLogDao extends BaseDao<TaskLog> {

    @Autowired
    public TaskLogDao(MongoTemplate mongoTemplate) {
        super(mongoTemplate, TaskLog.class);
    }

    public void deleteLogs(String taskId) {
        this.mongoTemplate.remove(Query.query(where("taskId").is(taskId)), TaskLog.class);
    }
}
