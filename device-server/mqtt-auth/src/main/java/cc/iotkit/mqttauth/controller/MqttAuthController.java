package cc.iotkit.mqttauth.controller;


import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.CodecUtil;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.mqttauth.model.EmqAcl;
import cc.iotkit.mqttauth.model.EmqAuthInfo;
import cc.iotkit.mqttauth.service.DeviceMqttAuth;
import cc.iotkit.mqttauth.service.MqttAuth;
import cc.iotkit.mqttauth.service.SysMqttAuth;
import cc.iotkit.mqttauth.service.WxMqttAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class MqttAuthController {

    @Autowired
    private DeviceMqttAuth deviceMqttAuth;
    @Autowired
    private WxMqttAuth wxMqttAuth;
    @Autowired
    private SysMqttAuth sysMqttAuth;

    @PostMapping("/mqtt/auth")
    public void auth(@RequestBody EmqAuthInfo auth) {
        log.info("mqtt auth:" + JsonUtil.toJsonString(auth));
        String clientId = auth.getClientid();
        if (isSupperUser(clientId)) {
            return;
        }
        MqttAuth mqttAuth = getMqttAuth(clientId);
        mqttAuth.auth(auth);
    }

    @PostMapping("/mqtt/acl")
    public void acl(@RequestBody EmqAcl acl) {
        log.info("mqtt acl:" + JsonUtil.toJsonString(acl));
        if (isSupperUser(acl.getClientid())) {
            return;
        }
        MqttAuth mqttAuth = getMqttAuth(acl.getClientid());
        mqttAuth.acl(acl);
    }

    @PostMapping("/mqtt/superuser")
    public void superuser(@RequestBody EmqAcl acl, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
//        log.info("mqtt superuser:" + JsonUtil.toJsonString(acl));
//        if (!isSupperUser(acl.getClientid())) {
//            throw new RuntimeException("superuser check false.");
//        }
    }

    public boolean isSupperUser(String clientId) {
        try {
            if (!clientId.startsWith("su_")) {
                return false;
            }
            clientId = clientId.replaceFirst("su_", "");
            return CodecUtil.aesDecrypt(clientId, Constants.MQTT_SECRET).startsWith("admin_");
        } catch (Throwable e) {
            log.error("aesDecrypt error.", e);
            return false;
        }
    }

    private MqttAuth getMqttAuth(String clientId) {
        if (clientId.startsWith("wx_")) {
            return wxMqttAuth;
        } else if (clientId.startsWith("sy_")) {
            return sysMqttAuth;
        }
        return deviceMqttAuth;
    }

}
