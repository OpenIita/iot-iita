package cc.iotkit.dao;

import cc.iotkit.model.protocol.ProtocolComponent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProtocolComponentRepository extends MongoRepository<ProtocolComponent, String> {
}
