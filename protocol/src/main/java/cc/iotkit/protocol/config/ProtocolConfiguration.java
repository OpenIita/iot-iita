package cc.iotkit.protocol.config;

import cc.iotkit.protocol.DeviceBehaviour;
import cc.iotkit.protocol.impl.DeviceBehaviourImpl;
import feign.Client;
import feign.Contract;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProtocolConfiguration {

    @Value("${protocol.server}")
    private String protocolServer;

    @Bean
    public DeviceBehaviour getDeviceBehaviour(Decoder decoder, Encoder encoder, Client client, Contract contract) {
        return new DeviceBehaviourImpl(protocolServer, decoder, encoder, client, contract);
    }

}
