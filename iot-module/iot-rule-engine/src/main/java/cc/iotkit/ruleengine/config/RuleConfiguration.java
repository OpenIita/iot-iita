/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.ruleengine.config;

import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.mq.MqConsumer;
import cc.iotkit.ruleengine.handler.RuleDeviceConsumer;
import cc.iotkit.ruleengine.rule.RuleMessageHandler;
import cc.iotkit.ruleengine.task.TaskManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class RuleConfiguration {

    @Bean
    public RuleDeviceConsumer getConsumer(MqConsumer<ThingModelMessage> consumer, RuleMessageHandler ruleMessageHandler) {
        return new RuleDeviceConsumer(consumer, Collections.singletonList(ruleMessageHandler));
    }

    @Bean
    public TaskManager getTaskManager() {
        return new TaskManager();
    }
}
