package cc.iotkit.dao;

import cc.iotkit.model.protocol.ProtocolComponent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProtocolComponentRepository extends MongoRepository<ProtocolComponent, String> {

    List<ProtocolComponent> findByState(String state);

    List<ProtocolComponent> findByStateAndType(String state, String type);

}
