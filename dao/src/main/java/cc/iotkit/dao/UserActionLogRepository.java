package cc.iotkit.dao;

import cc.iotkit.model.UserActionLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActionLogRepository extends MongoRepository<UserActionLog, String> {

}
