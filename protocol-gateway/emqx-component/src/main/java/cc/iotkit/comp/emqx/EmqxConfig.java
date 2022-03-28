package cc.iotkit.comp.emqx;

import lombok.Data;

import java.util.List;

@Data
public class EmqxConfig {

    private int authPort;

    private String broker;

    private int port;

    private boolean ssl;

    private String clientId;

    private String username;

    private String password;

    private List<String> subscribeTopics;
}
