package cc.iotkit.comp.websocket.server;

import lombok.Data;

import java.util.List;

@Data
public class WebSocketServerConfig {

    private int port;

    private String sslKey;

    private String sslCert;

    private boolean ssl;

    private List<AccessToken> accessTokens;

    @Data
    public static class AccessToken{
        private String tokenName;
        private String tokenStr;
    }
}
