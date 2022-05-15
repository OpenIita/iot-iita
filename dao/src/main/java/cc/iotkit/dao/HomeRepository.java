package cc.iotkit.dao;

import cc.iotkit.model.space.Home;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeRepository extends MongoRepository<Home, String> {

    List<Home> findByUid(String uid);

    Home findByUidAndCurrent(String uid,boolean current);
}
