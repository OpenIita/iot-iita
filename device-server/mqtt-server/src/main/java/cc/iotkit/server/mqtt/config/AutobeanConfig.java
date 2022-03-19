package cc.iotkit.server.mqtt.config;

import cc.iotkit.protocol.client.DeviceBehaviourClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutobeanConfig {

    @Value("${gateway.server:}")
    private String gatewayServer;

    @Bean
    public DeviceBehaviourClient getDeviceBehaviourClient() {
        return new DeviceBehaviourClient(gatewayServer);
    }

}
