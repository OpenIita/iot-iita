package cc.iotkit.mqttauth.service;

import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.mqttauth.config.Constants;
import cc.iotkit.mqttauth.model.EmqAcl;
import cc.iotkit.mqttauth.model.EmqAuthInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component("DeviceMqttAuth")
public class DeviceMqttAuth implements MqttAuth {
    @Autowired
    private DeviceService deviceService;

    @Override
    public void auth(EmqAuthInfo auth) {

        String clientId = auth.getClientid();
        String[] pkDnAndModel = getPkDnAndModel(clientId);

        String hmac = DigestUtils.md5Hex(Constants.MQTT_SECRET + clientId);
        if (!hmac.equalsIgnoreCase(auth.getPassword())) {
            throw new RuntimeException("password is illegal.");
        }

        DeviceInfo device = new DeviceInfo();
        device.setProductKey(pkDnAndModel[0]);
        device.setDeviceName(pkDnAndModel[1]);
        device.setModel(pkDnAndModel[2]);
        deviceService.register(device);
    }

    @Override
    public void acl(EmqAcl acl) {
        String[] pkDn = getPkDnFromTopic(acl.getTopic());
        String pk = pkDn[2];
        String dn = pkDn[3];
        DeviceInfo device = deviceService.getByPkAndDn(pk, dn);
        if (device == null) {
            log.error("the device is not registered,pk:{},dn:{}", pk, dn);
            return;
        }

        deviceService.online(pk, dn);
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
