package cc.iotkit.protocol.server.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ProtocolConfig {

    @Value("${pulsar.broker}")
    private String pulsarBrokerUrl;

    @Value("${pulsar.service}")
    private String pulsarServiceUrl;

}
