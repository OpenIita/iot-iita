package cc.iotkit.comp.emqx;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comp.AbstractComponent;
import cc.iotkit.comp.CompConfig;
import cc.iotkit.comp.model.DeviceState;
import cc.iotkit.converter.DeviceMessage;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import io.vertx.mqtt.messages.MqttConnAckMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class EmqxComponent extends AbstractComponent {

    private Vertx vertx;
    private AuthVerticle authVerticle;
    private CountDownLatch countDownLatch;
    private String deployedId;
    private EmqxConfig mqttConfig;

    public void create(CompConfig config) {
        super.create(config);
        vertx = Vertx.vertx();
        mqttConfig = JsonUtil.parse(config.getOther(), EmqxConfig.class);
        authVerticle = new AuthVerticle(mqttConfig);
    }

    @Override
    public void start() {
        try {
            authVerticle.setExecutor(getHandler());
            countDownLatch = new CountDownLatch(1);
            Future<String> future = vertx.deployVerticle(authVerticle);
            future.onSuccess((s -> {
                deployedId = s;
                countDownLatch.countDown();
            }));
            future.onFailure((e) -> {
                countDownLatch.countDown();
                log.error("start emqx auth component failed", e);
            });
            countDownLatch.await();

            MqttClientOptions options = new MqttClientOptions()
                    .setClientId(mqttConfig.getClientId())
                    .setUsername(mqttConfig.getUsername())
                    .setPassword(mqttConfig.getPassword())
                    .setCleanSession(true)
                    .setKeepAliveInterval(60);

            if (mqttConfig.isSsl()) {
                options.setSsl(true)
                        .setTrustAll(true);
            }
            MqttClient client = MqttClient.create(vertx, options);

            Future<MqttConnAckMessage> connFuture =
                    client.connect(mqttConfig.getPort(), mqttConfig.getBroker());
            connFuture.onSuccess(ack -> log.info("connect emqx broker success"))
                    .onFailure(e -> log.error("connect emqx broker failed", e));

            List<String> topics = mqttConfig.getSubscribeTopics();
            Map<String, Integer> subscribes = new HashMap<>();
            for (String topic : topics) {
                subscribes.put(topic, 1);
            }

            client.publishHandler(s -> {
                        String topic = s.topicName();
                        String payload = s.payload().toString();
                        log.info("receive message,topic:{},payload:{}", topic, payload);

//
//                //取消订阅
//                if (topic.equals("/sys/session/topic/unsubscribed")) {
//                    topicUnsubscribed(payload);
//                    return;
//                }
//
//                //连接断开
//                if (topic.equals("/sys/client/disconnected")) {
//                    disconnectedHandler.handler(payload);
//                    return;
//                }
//
//                String[] parts = topic.split("/");
//                if (parts.length < 5) {
//                    log.error("message topic is illegal.");
//                    return;
//                }
//                String productKey = parts[2];
//                String deviceName = parts[3];
//
//                //子设备注册
//                if (topic.endsWith("/register")) {


                        Map<String, Object> head = new HashMap<>();
                        head.put("topic", topic);
                        getHandler().onReceive(head, "", payload);
                    }).subscribe(subscribes).onSuccess(a -> log.info("subscribe topic success"))
                    .onFailure(e -> log.error("subscribe topic failed", e));

        } catch (Throwable e) {
            throw new BizException("start emqx auth component error", e);
        }
    }

    @SneakyThrows
    @Override
    public void stop() {
        authVerticle.stop();
        Future<Void> future = vertx.undeploy(deployedId);
        future.onSuccess(unused -> log.info("stop emqx auth component success"));
    }

    @Override
    public void destroy() {

    }

    @Override
    public void onDeviceStateChange(DeviceState state) {

    }

    @Override
    public void send(DeviceMessage message) {

    }

    @Override
    public boolean exist(String productKey, String deviceName) {
        return false;
    }

}
