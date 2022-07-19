package cc.iotkit.temporal.es.service;

import cc.iotkit.model.Paging;
import cc.iotkit.model.rule.RuleLog;
import cc.iotkit.temporal.IRuleLogData;
import org.springframework.stereotype.Service;

@Service
public class RuleLogDataImpl implements IRuleLogData {
    @Override
    public void deleteByRuleId(String ruleId) {

    }

    @Override
    public Paging<RuleLog> findByRuleId(String ruleId, int page, int size) {
        return null;
    }

    @Override
    public void add(RuleLog log) {

    }
}
