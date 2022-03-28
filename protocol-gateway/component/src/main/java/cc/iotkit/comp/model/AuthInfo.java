package cc.iotkit.comp.model;

import lombok.Data;

@Data
public class AuthInfo {

    private String productKey;

    private String deviceName;

    private String productSecret;

    private String deviceSecret;

}
