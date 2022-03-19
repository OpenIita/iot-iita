package cc.iotkit.server.mqtt.handler;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.protocol.client.DeviceBehaviourClient;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DisconnectedHandler {

    @Autowired
    private DeviceBehaviourClient behaviourClient;

    public void handler(String msg) {
        Disconnected disconnected = JsonUtil.parse(msg, new TypeReference<Disconnected>() {
        });
        String clientId = disconnected.getClientid();
        String[] parts = clientId.split("_");
        if (parts.length < 2) {
            return;
        }
        String pk = parts[0];
        String dn = parts[1];
        behaviourClient.offline(pk, dn);
        log.info("client disconnected, offline,pk:{},dn:{}", pk, dn);
    }

    @Data
    private static class Disconnected {
        private String reason;
        private String clientid;
        private String username;
        private String peername;
        private String sockname;
    }

}
