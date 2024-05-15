/*
 *
 *  * | Licensed 未经许可不能去掉「OPENIITA」相关版权
 *  * +----------------------------------------------------------------------
 *  * | Author: xw2sy@163.com
 *  * +----------------------------------------------------------------------
 *
 *  Copyright [2024] [OPENIITA]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */
package cc.iotkit.ruleengine.rule;

import cc.iotkit.common.thing.ThingModelMessage;
import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.model.rule.RuleLog;
import cc.iotkit.ruleengine.action.Action;
import cc.iotkit.ruleengine.filter.Filter;
import cc.iotkit.ruleengine.listener.Listener;
import cc.iotkit.temporal.IRuleLogData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 规则执行器
 */
@Component
@Slf4j
public class RuleExecutor {

    @Lazy
    @Autowired
    private IRuleLogData ruleLogData;

    public void execute(ThingModelMessage message, Rule rule) {
        if (!doListeners(message, rule)) {
            log.info("The listener did not match the appropriate content,rule:{},{}", rule.getId(), rule.getName());
            return;
        }
        log.info("Start execute rule {},id:{}", rule.getName(), rule.getId());

        RuleLog ruleLog = new RuleLog();
        ruleLog.setId(UUID.randomUUID().toString());
        ruleLog.setRuleId(rule.getId());
        ruleLog.setState(RuleLog.STATE_MATCHED_LISTENER);

        try {
            if (!doFilters(rule, message)) {
                ruleLog.setState(RuleLog.STATE_UNMATCHED_FILTER);
                log.info("The filter did not match the appropriate content,rule:{},{}", rule.getId(), rule.getName());
                return;
            }
            ruleLog.setState(RuleLog.STATE_MATCHED_FILTER);

            //执行动作返回执行内容
            List<String> results = doActions(rule, message);
            //保存动作内容和状态
            ruleLog.setContent(JsonUtils.toJsonString(results));
            ruleLog.setState(RuleLog.STATE_EXECUTED_ACTION);
            ruleLog.setSuccess(true);
            log.info("rule execution completed,id:{}", rule.getId());
        } catch (Throwable e) {
            log.error("rule execution error,id:" + rule.getId(), e);
            ruleLog.setSuccess(false);
            ruleLog.setContent(e.toString());
        } finally {
            ruleLog.setLogAt(System.currentTimeMillis());
            ruleLogData.add(ruleLog);
        }
    }

    private boolean doListeners(ThingModelMessage message, Rule rule) {
        List<Listener<?>> listeners = rule.getListeners();
        for (Listener<?> listener : listeners) {
            if (listener.execute(message)) {
                //只要有一个监听器匹配到数据即可
                return true;
            }
        }
        return false;
    }

    private boolean doFilters(Rule rule, ThingModelMessage msg) {
        List<Filter<?>> filters = rule.getFilters();
        for (Filter<?> filter : filters) {
            //只要有一个过滤器未通过都不算通过
            if (!filter.execute(msg)) {
                return false;
            }
        }
        return true;
    }

    private List<String> doActions(Rule rule, ThingModelMessage msg) {
        List<String> results = new ArrayList<>();
        for (Action<?> action : rule.getActions()) {
            results.addAll(action.execute(msg));
        }
        return results;
    }

}
