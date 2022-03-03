package cc.iotkit.mqttauth.model;

import lombok.Data;

@Data
public class EmqAuthInfo {

    private String clientid;

    private String password;

    private String username;

    private String ipaddress;

    private String protocol;

}
