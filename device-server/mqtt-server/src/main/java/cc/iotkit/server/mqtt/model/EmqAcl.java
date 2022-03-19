package cc.iotkit.server.mqtt.model;

import lombok.Data;

@Data
public class EmqAcl {

    private String access;

    private String username;

    private String clientid;

    private String ipaddr;

    private String protocol;

    private String topic;
}
