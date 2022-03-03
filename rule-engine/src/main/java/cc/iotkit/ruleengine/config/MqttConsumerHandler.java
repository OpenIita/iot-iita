package cc.iotkit.ruleengine.config;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.ruleengine.scene.SceneMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Slf4j
public class MqttConsumerHandler implements MessageHandler {

    @Autowired
    private SceneMessageHandler sceneMessageHandler;

    @Override
    public void handleMessage(Message<?> msg) throws MessagingException {
        String topic = (String) msg.getHeaders().get("mqtt_receivedTopic");
        if (topic == null) {
            log.warn("topic is null");
            return;
        }
        Object payload = msg.getPayload();
        if (!(payload instanceof String)) {
            payload = JsonUtil.toJsonString(payload);
        }
        log.info("receive message,topic:{},payload:{}", topic, payload);
        sceneMessageHandler.handler(topic, payload.toString());
    }

}
