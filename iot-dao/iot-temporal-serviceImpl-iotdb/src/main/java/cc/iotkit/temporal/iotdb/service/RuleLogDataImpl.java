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
package cc.iotkit.temporal.iotdb.service;

import cc.iotkit.common.api.Paging;
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
        return new Paging<>();
    }

    @Override
    public void add(RuleLog log) {
    }
}
