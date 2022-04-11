package cc.iotkit.dao;

import cc.iotkit.model.rule.RuleLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleLogRepository extends ElasticsearchRepository<RuleLog, String> {

    void deleteByRuleId(String ruleId);

    Page<RuleLog> findByRuleId(String ruleId, Pageable pageable);

}
