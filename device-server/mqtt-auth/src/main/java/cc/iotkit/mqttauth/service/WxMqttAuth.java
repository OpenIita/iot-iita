package cc.iotkit.mqttauth.service;

import cc.iotkit.mqttauth.model.EmqAcl;
import cc.iotkit.mqttauth.model.EmqAuthInfo;
import org.springframework.stereotype.Component;

@Component("WxMqttAuth")
public class WxMqttAuth implements MqttAuth {
    @Override
    public void auth(EmqAuthInfo auth) {

    }

    @Override
    public void acl(EmqAcl acl) {

    }
}
