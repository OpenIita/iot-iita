package cc.iotkit.dao;

import cc.iotkit.model.device.message.ThingModelMessage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThingModelMessageRepository extends ElasticsearchRepository<ThingModelMessage, String> {
}
