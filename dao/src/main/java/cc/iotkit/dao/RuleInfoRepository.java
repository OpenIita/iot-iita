package cc.iotkit.dao;

import cc.iotkit.model.rule.RuleInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleInfoRepository extends MongoRepository<RuleInfo, String> {

    List<RuleInfo> findByUidAndType(String uid, String type);

}
