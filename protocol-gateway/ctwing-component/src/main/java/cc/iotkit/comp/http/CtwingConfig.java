package cc.iotkit.comp.http;

import lombok.Data;

@Data
public class CtwingConfig {

    private int port;

    /**
     * ctwing推送消息加解密token
     */
    private String encryptToken;

    /**
     * ctwing应用的appKey
     */
    private String appKey;

    /**
     * ctwing应用的appSecret
     */
    private String appSecret;

}
