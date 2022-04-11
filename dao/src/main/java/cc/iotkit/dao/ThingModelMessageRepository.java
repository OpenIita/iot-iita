package cc.iotkit.dao;

import cc.iotkit.model.device.message.ThingModelMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThingModelMessageRepository extends ElasticsearchRepository<ThingModelMessage, String> {

    Page<ThingModelMessage> findByTypeAndIdentifier(String type, String identifier, Pageable pageable);

}
