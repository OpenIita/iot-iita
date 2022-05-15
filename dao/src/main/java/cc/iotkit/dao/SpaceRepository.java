package cc.iotkit.dao;

import cc.iotkit.model.space.Space;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceRepository extends MongoRepository<Space, String> {

    List<Space> findByUidOrderByCreateAtDesc(String uid);

    List<Space> findByUidAndHomeIdOrderByCreateAtDesc(String uid, String homeId);

}
