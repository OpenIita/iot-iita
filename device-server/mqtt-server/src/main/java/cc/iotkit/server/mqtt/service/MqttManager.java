package cc.iotkit.server.mqtt.service;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.CodecUtil;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.protocol.DeviceMessage;
import cc.iotkit.protocol.RegisterInfo;
import cc.iotkit.protocol.client.DeviceBehaviourClient;
import cc.iotkit.server.mqtt.handler.DisconnectedHandler;
import cc.iotkit.server.mqtt.model.Request;
import cc.iotkit.server.mqtt.model.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MqttManager implements MqttCallback, IMqttMessageListener {

    @Value("${mqtt.url}")
    private String url;

    @Value(("${spring.profiles.active:}"))
    private String env;

    private MqttClient mqttClient;

    @Autowired
    private DisconnectedHandler disconnectedHandler;
    @Autowired
    private DeviceBehaviourClient behaviourClient;

    public MqttManager() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleWithFixedDelay(this::createClient, 1, 3, TimeUnit.SECONDS);
    }

    private MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
        // 这里设置为true表示每次连接到服务器都以新的身份连接
        options.setCleanSession(true);
        // 设置连接的用户名
        options.setUserName("admin");
        // 设置连接的密码
        options.setPassword("password".toCharArray());
        options.setServerURIs(StringUtils.split(url, ","));
        // 设置超时时间 单位为秒
        options.setConnectionTimeout(10);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线，但这个方法并没有重连的机制
        options.setKeepAliveInterval(20);
        return options;
    }

    @SneakyThrows
    private void createClient() {
        try {
            if (mqttClient == null) {
                MemoryPersistence persistence = new MemoryPersistence();
                String clientId = "mqtt-server-consumer-" + env;
                clientId = "su_" + CodecUtil.aesEncrypt("admin_" + clientId, Constants.MQTT_SECRET);
                mqttClient = new MqttClient(url, clientId, persistence);
                mqttClient.setCallback(this);
            }
            if (mqttClient.isConnected()) {
                return;
            }
            connect();
        } catch (Throwable e) {
            log.error("create mqttClient error", e);
        }
    }

    private void connect() {
        try {
            log.info("Connecting to broker:{} ", url);
            IMqttToken mqttToken = mqttClient.connectWithResult(getMqttConnectOptions());
            if (mqttToken.isComplete()) {
                log.info("connect mqtt-broker success");
            } else {
                log.error("connect mqtt-broker failed", mqttToken.getException());
            }
            IMqttToken response = mqttClient.subscribeWithResponse(
                    new String[]{"/sys/+/+/s/#", "/sys/session/topic/unsubscribed", "/sys/client/disconnected"});
            if (response.isComplete()) {
                log.info("subscribe topics success");
            } else {
                log.error("subscribe topics failed", mqttToken.getException());
            }
        } catch (Throwable e) {
            log.error("connect to mqtt-broker error", e);
        }
    }

    @SneakyThrows
    @Override
    public void connectionLost(Throwable e) {
        log.error("mqtt connection lost", e);
        while (true) {
            try {
                Thread.sleep(1000);
                if (mqttClient.isConnected()) {
                    mqttClient.disconnect();
                }
                connect();
                break;
            } catch (Throwable e1) {
                log.error("connect error,retry...", e1);
            }
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        handleMessage(topic, mqttMessage);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }

    public void handleMessage(String topic, MqttMessage msg) {
        log.info("receive msg,topic:{},msg:{}", topic, JsonUtil.toJsonString(msg));
        if (topic == null) {
            log.error("message topic is null.");
            return;
        }
        int code = 0;
        String mid = "";

        try {
            String payload = new String(msg.getPayload());

            //取消订阅
            if (topic.equals("/sys/session/topic/unsubscribed")) {
                topicUnsubscribed(payload);
                return;
            }

            //连接断开
            if (topic.equals("/sys/client/disconnected")) {
                disconnectedHandler.handler(payload);
                return;
            }

            String[] parts = topic.split("/");
            if (parts.length < 5) {
                log.error("message topic is illegal.");
                return;
            }
            String productKey = parts[2];
            String deviceName = parts[3];

            //子设备注册
            if (topic.endsWith("/register")) {
                RegisterRequest registerRequest = JsonUtil.parse(payload, RegisterRequest.class);
                mid = registerRequest.getId();

                RegisterInfo registerInfo = RegisterInfo.builder()
                        .productKey(productKey)
                        .deviceName(deviceName)
                        .subDevices(Collections.singletonList(registerRequest.getParams()))
                        .build();
                behaviourClient.register(registerInfo);
                return;
            }

            Request<?> request = JsonUtil.parse(payload, Request.class);
            mid = request.getId();
            MqtMsg mqtMsg = new MqtMsg(topic, request);

            behaviourClient.messageReport(DeviceMessage.builder()
                    .productKey(productKey)
                    .deviceName(deviceName)
                    .mid(request.getId())
                    .content(JsonUtil.toJsonString(mqtMsg))
                    .build());

        } catch (Throwable e) {
            log.error("message process error", e);
            code = 500;
        } finally {
            reply(topic, mid, code);
        }

    }

    @SneakyThrows
    private void reply(String topic, String id, int code) {
        //回复消息不需要再回复
        if (topic.endsWith("_reply")) {
            return;
        }

        topic = topic.replace("/s/", "/c/") + "_reply";
        String msg = JsonUtil.toJsonString(new Response<>(id, code, new HashMap<>()));
        mqttClient.publish(topic, new MqttMessage(msg.getBytes()));
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
        behaviourClient.offline(parts[2], parts[3]);
    }

    @SneakyThrows
    public void sendMsg(String topic, String msg) {
        mqttClient.publish(topic, new MqttMessage(msg.getBytes()));
    }

    @Data
    private static class Unsubscribed {
        private String clientid;
        private String username;
        private String topic;
        private String peerhost;
    }

    public static class RegisterRequest extends Request<RegisterInfo.SubDevice> {

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MqtMsg {

        private String topic;

        private Object payload;

    }

}
