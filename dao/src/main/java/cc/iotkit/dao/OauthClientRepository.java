package cc.iotkit.dao;

import cc.iotkit.model.OauthClient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthClientRepository extends MongoRepository<OauthClient, String> {


}
