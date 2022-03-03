package cc.iotkit.dao;

import cc.iotkit.model.AppInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppInfoRepository extends MongoRepository<AppInfo, String> {
}
