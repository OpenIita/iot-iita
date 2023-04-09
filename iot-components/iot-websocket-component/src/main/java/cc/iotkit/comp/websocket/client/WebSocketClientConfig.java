package cc.iotkit.comp.websocket.client;

import lombok.Data;

@Data
public class WebSocketClientConfig {

    private int port;

    private String ip;

    private String url;

    private long heartBeatTime;

    private String heartBeatData;

    private boolean ssl;

}
