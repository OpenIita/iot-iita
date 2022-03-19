package cc.iotkit.server.mqtt.service;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.protocol.RegisterInfo;
import cc.iotkit.protocol.Result;
import cc.iotkit.protocol.client.DeviceBehaviourClient;
import cc.iotkit.server.mqtt.model.EmqAcl;
import cc.iotkit.server.mqtt.model.EmqAuthInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeviceAuthService {

    @Autowired
    private DeviceBehaviourClient behaviourClient;

    public void auth(EmqAuthInfo auth) {
        String clientId = auth.getClientid();
        String[] pkDnAndModel = getPkDnAndModel(clientId);

        String hmac = DigestUtils.md5Hex(Constants.MQTT_SECRET + clientId);
        if (!hmac.equalsIgnoreCase(auth.getPassword())) {
            throw new RuntimeException("password is illegal.");
        }

        String pk = pkDnAndModel[0];
        String dn = pkDnAndModel[1];
        String model = pkDnAndModel[2];

        Result result = behaviourClient.register(new RegisterInfo(pk, dn, model));
        log.info("register result:{}", JsonUtil.toJsonString(result));
    }

    public void acl(EmqAcl acl) {
        String[] pkDn = getPkDnFromTopic(acl.getTopic());
        String pk = pkDn[2];
        String dn = pkDn[3];
        behaviourClient.online(pk, dn);
    }

    private String[] getPkDnAndModel(String clientId) {
        if (StringUtils.isBlank(clientId)) {
            throw new RuntimeException("clientId is blank.");
        }
        clientId += "_";

        String[] pkDnAndModel = clientId.split("_", -1);
        if (pkDnAndModel.length < 3) {
            throw new RuntimeException("clientId is illegal.");
        }
        return pkDnAndModel;
    }

    private String[] getPkDnFromTopic(String topic) {
        if (StringUtils.isBlank(topic)) {
            throw new RuntimeException("topic is blank.");
        }

        String[] pkDn = topic.split("/", -1);
        if (pkDn.length < 4) {
            throw new RuntimeException("topic is illegal.");
        }
        return pkDn;
    }
}
