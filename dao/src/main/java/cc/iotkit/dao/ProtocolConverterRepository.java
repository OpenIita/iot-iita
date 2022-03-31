package cc.iotkit.dao;

import cc.iotkit.model.protocol.ProtocolConverter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProtocolConverterRepository extends MongoRepository<ProtocolConverter, String> {
}
