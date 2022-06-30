package cc.iotkit.dao;

import cc.iotkit.model.ThirdUserSession;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ThirdUserSessionRepository extends ElasticsearchRepository<ThirdUserSession, String> {


}
