package cc.iotkit.ruleengine.config;

import cc.iotkit.ruleengine.handler.RuleDeviceConsumer;
import cc.iotkit.ruleengine.rule.RuleMessageHandler;
import cc.iotkit.ruleengine.task.TaskManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class RuleConfiguration {

    @Value("${pulsar.broker}")
    public String pulsarBroker;

    @Bean
    public RuleDeviceConsumer getConsumer(RuleMessageHandler ruleMessageHandler) {
        return new RuleDeviceConsumer(pulsarBroker, Collections.singletonList(ruleMessageHandler));
    }

    @Bean
    public TaskManager getTaskManager() {
        return new TaskManager();
    }
}
