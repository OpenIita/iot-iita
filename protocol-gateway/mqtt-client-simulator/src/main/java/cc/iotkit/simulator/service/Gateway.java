package cc.iotkit.simulator.service;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.simulator.config.Mqtt;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.ConnectException;
import java.util.*;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class Gateway extends Device {

    private MqttClient client;

    private List<Device> subDevices = new ArrayList<>();

    public Gateway(String productKey, String deviceName) {
        super(productKey, deviceName, "GW01");
    }

    @SneakyThrows
    public void start() {
        String broker = Mqtt.broker;

        String clientId = String.format("%s_%s_%s", productKey, deviceName, getModel());
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient client = new MqttClient(broker, clientId, persistence);

            // MQTT 连接选项
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(this.deviceName);
            connOpts.setPassword(DigestUtils.md5Hex(Constants.PRODUCT_SECRET + clientId).toCharArray());
            // 保留会话
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(10);

            // 设置回调
            client.setCallback(new OnMessageCallback(client, this));

            // 建立连接
            log.info("Connecting to broker:{} ", broker);
            try {
                IMqttToken result = client.connectWithResult(connOpts);
                result.waitForCompletion();
            } catch (Throwable e) {
                log.error("connect failed,reconnect...");
                Thread.sleep(3000);
                this.start();
            }

            log.info("Connected");
            // 订阅
            String topic = String.format("/sys/%s/%s/c/#", productKey, deviceName);
            log.info("subscribe topic:{}", topic);
            while (!client.isConnected()) {
                Thread.sleep(100);
            }

            IMqttToken mqttToken = client.subscribeWithResponse(topic);
            if (mqttToken.isComplete()) {
                //注册子设备
                for (Device subDevice : subDevices) {
                    Request request = new Request();
                    request.setId(UUID.randomUUID().toString());
                    request.setParams(subDevice);
                    topic = String.format("/sys/%s/%s/s/register", productKey, deviceName);
                    String payload = JsonUtil.toJsonString(request);
                    client.publish(String.format("/sys/%s/%s/s/register", productKey, deviceName),
                            new MqttMessage(payload.getBytes())
                    );
                    log.info("publish message,topic:{},payload:{}", topic, payload);
                }
            }
        } catch (Throwable e) {
            log.error("connect mqtt-broker error", e);
        }
    }

    public void addSubDevice(String productKey, String deviceName, String model) {
        subDevices.add(new Device(productKey, deviceName, model));
    }

    @Data
    public static class OnMessageCallback implements MqttCallback {

        private MqttClient client;
        private Gateway gateway;

        public OnMessageCallback(MqttClient client, Gateway gateway) {
            this.client = client;
            this.gateway = gateway;
        }

        @SneakyThrows
        public void connectionLost(Throwable cause) {
            log.info("连接断开，重连...");
            Thread.sleep(3000);
            client.close();
            gateway.start();
        }

        @SneakyThrows
        public void messageArrived(String topic, MqttMessage message) {
            try {
                log.info("接收消息,topic:{},payload:{}", topic,
                        new String(message.getPayload()));

                if (topic.endsWith("register_reply")) {
                    String payload = new String(message.getPayload());
                    Response response = JsonUtil.parse(payload, Response.class);
                    //子设备注册成功
                    if (response.code == 0) {
                        Map<String, Object> data = response.getData();
                        //订阅子设备消息
                        String subTopic = String.format("/sys/%s/%s/c/#",
                                data.get("productKey"), data.get("deviceName"));
                        log.info("subscribe topic:{}", subTopic);
                        client.subscribe(subTopic);
                    }
                }

                if (topic.endsWith("_reply")) {
                    return;
                }
                String payload = new String(message.getPayload());
                Request request = JsonUtil.parse(payload, Request.class);

                Response response = new Response(request.getId(), 0, new HashMap<>());
                client.publish(topic.replace("/c/", "/s/") + "_reply",
                        new MqttMessage(JsonUtil.toJsonString(response).getBytes()));

                //属性设置后上报属性
                String setTopic = "/c/service/property/set";
                if (topic.endsWith(setTopic)) {
                    request.setId(UUID.randomUUID().toString());
                    client.publish(topic.replace(setTopic, "/s/event/property/post"),
                            new MqttMessage(JsonUtil.toJsonString(request).getBytes()));
                }
            } catch (Throwable e) {
                log.info("receive msg error", e);
            }
        }

        public void deliveryComplete(IMqttDeliveryToken token) {
            log.info("deliveryComplete,topic:{},result:{}", token.getTopics(), token.isComplete());
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private String id;

        private Object params;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private String id;

        private int code;

        private Map<String, Object> data;
    }

}
