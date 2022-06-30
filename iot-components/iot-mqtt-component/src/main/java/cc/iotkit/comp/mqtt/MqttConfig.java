package cc.iotkit.comp.mqtt;

import lombok.Data;

@Data
public class MqttConfig {

    private int port;

    private String sslKey;

    private String sslCert;

    private boolean ssl;

}
