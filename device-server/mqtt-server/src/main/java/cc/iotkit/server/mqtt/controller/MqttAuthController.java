package cc.iotkit.server.mqtt.controller;


import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.CodecUtil;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.server.mqtt.model.EmqAcl;
import cc.iotkit.server.mqtt.model.EmqAuthInfo;
import cc.iotkit.server.mqtt.service.DeviceAuthService;
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
    private DeviceAuthService deviceAuthService;

    @PostMapping("/mqtt/auth")
    public void auth(@RequestBody EmqAuthInfo auth) {
        log.info("mqtt auth:" + JsonUtil.toJsonString(auth));
        String clientId = auth.getClientid();
        if (isSupperUser(clientId)) {
            return;
        }
        deviceAuthService.auth(auth);
    }

    @PostMapping("/mqtt/acl")
    public void acl(@RequestBody EmqAcl acl) {
        log.info("mqtt acl:{}", JsonUtil.toJsonString(acl));
        if (isSupperUser(acl.getClientid())) {
            return;
        }
        deviceAuthService.acl(acl);
        log.info("topic:{}, acl success", acl.getTopic());
    }

    @PostMapping("/mqtt/superuser")
    public void superuser(@RequestBody EmqAcl acl, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
    }

    public boolean isSupperUser(String clientId) {
        try {
            if (!clientId.startsWith("su_")) {
                return false;
            }
            clientId = clientId.replaceFirst("su_", "");
            return CodecUtil.aesDecrypt(clientId, Constants.PRODUCT_SECRET).startsWith("admin_");
        } catch (Throwable e) {
            log.error("aesDecrypt error.", e);
            return false;
        }
    }

}
