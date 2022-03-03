package cc.iotkit.mqttauth.service;

import cc.iotkit.common.utils.CodecUtil;
import cc.iotkit.mqttauth.config.Constants;
import cc.iotkit.mqttauth.model.EmqAcl;
import cc.iotkit.mqttauth.model.EmqAuthInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component("SysMqttAuth")
public class SysMqttAuth implements MqttAuth {
    @Override
    public void auth(EmqAuthInfo auth) {
        try {
            //password= aes(sy_username,ACCOUNT_SECRET)
            String uid = auth.getUsername();
            String codes = CodecUtil.aesDecryptHex(auth.getPassword(), Constants.ACCOUNT_SECRET);
            if (StringUtils.isBlank(codes)) {
                throw new RuntimeException("mqtt auth failed,pwd error.");
            }
            //解出来的用户id与username是否一致
            String[] parts = codes.split("_");
            if (parts.length < 2 || !uid.equals(parts[1])) {
                throw new RuntimeException("mqtt auth failed,pw validate error.");
            }
        } catch (Throwable e) {
            log.error("sys user mqtt failed.", e);
            throw new RuntimeException("mqtt auth failed:" + e.getMessage());
        }
    }

    @Override
    public void acl(EmqAcl acl) {
        //平台用户可订阅以所有设备
//        String topic = acl.getTopic();
//        if (!topic.startsWith("/app/")) {
//            throw new RuntimeException("acl failed.");
//        }
    }
}
