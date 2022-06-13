package cc.iotkit.ruleengine.config;

import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.mq.MqConsumer;
import cc.iotkit.mq.MqProducer;
import cc.iotkit.mq.vertx.VertxMqConsumer;
import cc.iotkit.mq.vertx.VertxMqProducer;
import cc.iotkit.ruleengine.handler.RuleDeviceConsumer;
import cc.iotkit.ruleengine.rule.RuleMessageHandler;
import cc.iotkit.ruleengine.task.TaskManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class RuleConfiguration {

    @Bean
    public RuleDeviceConsumer getConsumer(MqConsumer<ThingModelMessage> consumer, RuleMessageHandler ruleMessageHandler) {
        return new RuleDeviceConsumer(consumer, Collections.singletonList(ruleMessageHandler));
    }

    @ConditionalOnMissingBean
    @Bean
    public MqConsumer<ThingModelMessage> getThingModelMessageConsumer() {
        return new VertxMqConsumer<>(ThingModelMessage.class);
    }

    @Bean
    public TaskManager getTaskManager() {
        return new TaskManager();
    }
}
