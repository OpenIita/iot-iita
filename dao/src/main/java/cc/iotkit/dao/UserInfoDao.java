package cc.iotkit.dao;

import cc.iotkit.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserInfoDao extends BaseDao<UserInfo> {

    @Autowired
    public UserInfoDao(MongoTemplate mongoTemplate) {
        super(mongoTemplate, UserInfo.class);
    }
}
