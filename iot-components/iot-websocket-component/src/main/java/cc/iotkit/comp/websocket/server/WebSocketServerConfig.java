package cc.iotkit.comp.websocket.server;

import lombok.Data;

@Data
public class WebSocketServerConfig {

    private int port;

    private String sslKey;

    private String sslCert;

    private boolean ssl;

}
