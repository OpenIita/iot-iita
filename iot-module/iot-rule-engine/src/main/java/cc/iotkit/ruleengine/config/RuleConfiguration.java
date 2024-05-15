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
package cc.iotkit.ruleengine.config;

import cc.iotkit.common.thing.ThingModelMessage;
import cc.iotkit.mq.MqConsumer;
import cc.iotkit.ruleengine.handler.RuleDeviceConsumer;
import cc.iotkit.ruleengine.task.TaskManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RuleConfiguration {

    @Bean
    public RuleDeviceConsumer getConsumer(MqConsumer<ThingModelMessage> consumer) {
        return new RuleDeviceConsumer(consumer);
    }

    @Bean
    public TaskManager getTaskManager() {
        return new TaskManager();
    }
}
