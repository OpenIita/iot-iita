package cc.iotkit.dao;

import cc.iotkit.model.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoRepository extends MongoRepository<UserInfo, String> {

    List<UserInfo> findByType(int type);

    List<UserInfo> findByTypeAndOwnerId(int type, String ownerId);

}
