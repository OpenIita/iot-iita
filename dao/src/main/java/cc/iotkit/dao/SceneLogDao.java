package cc.iotkit.dao;

import cc.iotkit.model.rule.SceneLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class SceneLogDao extends BaseDao<SceneLog> {

    @Autowired
    public SceneLogDao(MongoTemplate mongoTemplate) {
        super(mongoTemplate, SceneLog.class);
    }

    public void deleteLogs(String sceneId) {
        this.mongoTemplate.remove(Query.query(where("sceneId").is(sceneId)), SceneLog.class);
    }
}
