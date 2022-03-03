package cc.iotkit.mqttauth.service;

import cc.iotkit.mqttauth.model.EmqAcl;
import cc.iotkit.mqttauth.model.EmqAuthInfo;

public interface MqttAuth {

    void auth(EmqAuthInfo auth);

    void acl(EmqAcl acl);

}
