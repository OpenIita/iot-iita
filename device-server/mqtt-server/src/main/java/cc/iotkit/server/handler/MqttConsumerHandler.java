package cc.iotkit.server.handler;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.server.config.Constants;
import cc.iotkit.server.dao.DeviceDao;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.mq.Request;
import cc.iotkit.model.mq.Response;
import cc.iotkit.server.service.DeviceService;
import cc.iotkit.server.service.IMqttSender;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MqttConsumerHandler implements MessageHandler, ApplicationContextAware {

    private List<MqttHandler> mqttHandlers = new ArrayList<>();

    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private IMqttSender mqttSender;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DisconnectedHandler disconnectedHandler;

    @Override
    public void handleMessage(Message<?> msg) throws MessagingException {
        log.info(JsonUtil.toJsonString(msg));
        MessageHeaders headers = msg.getHeaders();
        String topic = headers.get("mqtt_receivedTopic", String.class);
        if (topic == null) {
            log.error("message topic is null.");
            return;
        }

        if (topic.equals("/sys/session/topic/unsubscribed")) {
            topicUnsubscribed(msg.getPayload().toString());
            return;
        }

        if (topic.equals("/sys/client/disconnected")) {
            disconnectedHandler.handler(msg.getPayload().toString());
            return;
        }

        String[] parts = topic.split("/");
        if (parts.length < 5) {
            log.error("message topic is illegal.");
            return;
        }

        String pk = parts[2];
        String dn = parts[3];
        DeviceInfo device = deviceDao.getByPkAndDn(pk, dn);
        if (device == null) {
            log.warn("device not found by pk and dn.");
            return;
        }
        String payload = msg.getPayload().toString();

        //转发到deviceId对应的topic中给客户端消费
        sendToAppClientTopic(device.getDeviceId(), topic, payload);

        Object result = null;
        Request<?> request = new Request<>();
        try {
            for (MqttHandler handler : mqttHandlers) {
                if (!handler.compliant(topic)) {
                    continue;
                }
                request = handler.parse(payload);
                result = handler.handler(topic, device, request);
            }
        } catch (Throwable e) {
            log.error("handler mqtt msg error.", e);
            reply(device.getDeviceId(), topic, request.getId(), 1, "");
            return;
        }

        if (result == null) {
            return;
        }

        reply(device.getDeviceId(), topic, request.getId(), 0, result);
    }

    private void reply(String deviceId, String topic, String id, int code, Object result) {
        topic = topic.replace("/s/", "/c/") + "_reply";
        String msg = JsonUtil.toJsonString(new Response<>(id, code, result));
        mqttSender.sendToMqtt(topic, msg);
        sendToAppClientTopic(deviceId, topic, msg);
    }

    private void topicUnsubscribed(String msg) {
        Unsubscribed unsubscribed = JsonUtil.parse(msg, new TypeReference<Unsubscribed>() {
        });
        String topic = unsubscribed.getTopic();
        String[] parts = topic.split("/");
        if (parts.length < 4) {
            return;
        }

        log.info("device offline,pk:{},dn:{}", parts[2], parts[3]);
        deviceService.offline(parts[2], parts[3]);
    }

    private void sendToAppClientTopic(String deviceId, String topic, String msg) {
        //排除服务调用和属性设置消息
        if (topic.contains("/c/service/") || topic.endsWith("/post_reply")) {
            return;
        }

        //发给app端订阅消息
        distributeMsg(Constants.TOPIC_PREFIX_APP, topic, deviceId, msg);
        //发送给第三方接入gateway
        distributeMsg(Constants.TOPIC_PREFIX_GATEWAY, topic, deviceId, msg);
    }

    /**
     * 分发消息
     */
    void distributeMsg(String topicNamePrefix, String topic, String deviceId, String msg) {
        /*
        /app/xxxdeviceId/event/事件名
        /app/xxxdeviceId/event/property/post
        /app/xxxdeviceId/service/服务名_reply
         */
        String distTopic = "/" + topicNamePrefix + "/" + deviceId + "/" +
                (topic.replaceAll("/sys/.*/s/", "")
                        .replaceAll("/sys/.*/c/", ""));
        log.info("send msg:{},to topic:{}", JsonUtil.toJsonString(msg), distTopic);
        //转发到deviceId对应的topic中给客户端消费
        mqttSender.sendToMqtt(distTopic, msg);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        mqttHandlers.addAll(context.getBeansOfType(MqttHandler.class).values());
    }

    @Data
    private static class Unsubscribed {
        private String clientid;
        private String username;
        private String topic;
        private String peerhost;
    }
}
