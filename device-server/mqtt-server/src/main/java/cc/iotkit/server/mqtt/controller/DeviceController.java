package cc.iotkit.server.mqtt.controller;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.protocol.DeviceGateway;
import cc.iotkit.protocol.DeviceMessage;
import cc.iotkit.protocol.OtaInfo;
import cc.iotkit.protocol.Result;
import cc.iotkit.server.mqtt.service.MqttManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DeviceController implements DeviceGateway {

    @Autowired
    private MqttManager mqttManager;

    @Override
    public Result sendMessage(DeviceMessage msg) {
        try {
            MqttMessage mqttMessage = JsonUtil.parse(msg.getContent(), MqttMessage.class);
            mqttManager.sendMsg(mqttMessage.getTopic(), mqttMessage.getPayload());
            return new Result(true, "");
        } catch (Throwable e) {
            log.error("send message error", e);
            return new Result(false, e.getMessage());
        }
    }

    @Override
    public Result sendOta(OtaInfo ota) {
        return null;
    }

    @Data
    public static class MqttMessage {

        private String topic;

        private String payload;

    }

}
