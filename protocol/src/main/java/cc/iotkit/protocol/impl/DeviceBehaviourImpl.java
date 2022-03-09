package cc.iotkit.protocol.impl;

import cc.iotkit.protocol.*;
import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;

@Import(FeignClientsConfiguration.class)
public class DeviceBehaviourImpl implements DeviceBehaviour {

    private final Feign.Builder builder;

    private final String protocolServer;

    private DeviceBehaviour target;

    public DeviceBehaviourImpl(String protocolServer, Decoder decoder,
                               Encoder encoder, Client client, Contract contract) {
        this.protocolServer = protocolServer;
        this.builder = Feign.builder()
                .client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(contract);
    }

    private DeviceBehaviour behaviour() {
        if (target == null) {
            target = this.builder.target(DeviceBehaviour.class, protocolServer);
        }
        return target;
    }

    @Override
    public Result register(RegisterInfo info) {
        return behaviour().register(info);
    }

    @Override
    public Result deregister(DeregisterInfo info) {
        return behaviour().deregister(info);
    }

    @Override
    public void online(String productKey, String deviceName) {
        behaviour().online(productKey, deviceName);
    }

    @Override
    public void offline(String productKey, String deviceName) {
        behaviour().offline(productKey, deviceName);
    }

    @Override
    public void messageReport(DeviceMessage msg) {

    }

    @Override
    public void otaProgressReport(OtaMessage msg) {
    }
}
