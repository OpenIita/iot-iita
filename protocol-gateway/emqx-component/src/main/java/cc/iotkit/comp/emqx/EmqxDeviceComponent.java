package cc.iotkit.comp.emqx;

import cc.iotkit.common.Constants;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comp.AbstractDeviceComponent;
import cc.iotkit.comp.CompConfig;
import cc.iotkit.comp.IMessageHandler;
import cc.iotkit.comp.model.DeviceState;
import cc.iotkit.comp.utils.SpringUtils;
import cc.iotkit.converter.DeviceMessage;
import cc.iotkit.converter.ThingService;
import cc.iotkit.model.device.message.ThingModelMessage;
import com.fasterxml.jackson.databind.JsonNode;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import lombok.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.CountDownLatch;


public class EmqxDeviceComponent extends AbstractDeviceComponent {

    private static final Logger log = LoggerFactory.getLogger(EmqxDeviceComponent.class);
    private Vertx vertx;
    private AuthVerticle authVerticle;
    //private MqttVerticle mqttVerticle;
    private CountDownLatch countDownLatch;
    private String deployedId;
    private EmqxConfig mqttConfig;
    MqttClient client;

    //组件mqtt clientId，默认通过mqtt auth验证。
    private Set<String> compMqttClientIdList = new HashSet<>();


    private final Map<String, Device> deviceChildToParent = new HashMap<>();

    private TransparentConverter transparentConverter = new TransparentConverter();

    public void create(CompConfig config) {
        super.create(config);
        vertx = Vertx.vertx();
        mqttConfig = JsonUtil.parse(config.getOther(), EmqxConfig.class);
        authVerticle = new AuthVerticle(mqttConfig);
    }

    @Override
    public void start() {
        try {
            compMqttClientIdList.add(mqttConfig.getClientId());

            authVerticle.setExecutor(getHandler());
            countDownLatch = new CountDownLatch(1);
            Future<String> future = vertx.deployVerticle(authVerticle);
            future.onSuccess((s -> {
                deployedId = s;
                countDownLatch.countDown();
                log.error("start emqx auth component success", s);
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
            client = MqttClient.create(vertx, options);

            // handler will be called when we have a message in topic we subscribe for
            /*client.publishHandler(p -> {
                log.info("Client received message on [{}] payload [{}] with QoS [{}]", p.topicName(), p.payload().toString(Charset.defaultCharset()), p.qosLevel());
            });*/

            List<String> topics = mqttConfig.getSubscribeTopics();
            Map<String, Integer> subscribes = new HashMap<>();

            subscribes.put("/sys/+/+/s/#", 1);
            subscribes.put("/sys/client/connected", 1);
            subscribes.put("/sys/client/disconnected", 1);
            subscribes.put("/sys/session/subscribed", 1);
            subscribes.put("/sys/session/unsubscribed", 1);

            //"/sys/+/+/s/#","/sys/client/disconnected"

            /*for (String topic : topics) {
                subscribes.put(topic, 1);
            }*/

            // handler will be called when we have a message in topic we subscribe for
            client.publishHandler(p -> {
                log.info("Client received message on [{}] payload [{}] with QoS [{}]", p.topicName(), p.payload().toString(Charset.defaultCharset()), p.qosLevel());

                String topic = p.topicName();
                String payload = p.payload().toString();

                try {
                    IMessageHandler messageHandler = getHandler();
                    if (messageHandler != null) {
                        Map<String, Object> head = new HashMap<>();
                        head.put("topic", topic);
                        messageHandler.onReceive(head, "", payload);

                    }
                } catch (Exception e) {
                    log.error("message is illegal.", e);
                }
            });

            client.connect(mqttConfig.getPort(), mqttConfig.getBroker(), s -> {
                if (s.succeeded()) {
                    log.info("client connect success.");
                    client.subscribe(subscribes, e -> {
                        if (e.succeeded()) {
                            log.info("===>subscribe success: {}", e.result());
                        } else {
                            log.error("===>subscribe fail: ", e.cause());
                        }
                    });

                } else {
                    log.error("client connect fail: ", s.cause());
                }
            }).exceptionHandler(event -> {
                log.error("client fail: ", event.getCause());
            });

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
        client.disconnect()
                .onSuccess(unused -> log.info("stop emqx component success"))
                .onFailure(unused -> log.info("stop emqx component failure"));
    }

    @Override
    public void destroy() {

    }

    @Override
    public void onDeviceStateChange(DeviceState state) {
        DeviceState.Parent parent = state.getParent();
        if (parent == null) {
            return;
        }
        Device device = new Device(state.getProductKey(), state.getDeviceName());

        if (DeviceState.STATE_ONLINE.equals(state.getState())) {
            //保存子设备所属父设备
            deviceChildToParent.put(device.toString(),
                    new Device(parent.getProductKey(), parent.getDeviceName())
            );
        } else {
            //删除关系
            deviceChildToParent.remove(device.toString());
        }
    }

    @Override
    public void send(DeviceMessage message) {
        Device child = new Device(message.getProductKey(), message.getDeviceName());
        //作为子设备查找父设备
        Device parent = deviceChildToParent.get(child.toString());
        if (parent == null) {
            parent = child;
        }

        Object obj = message.getContent();
        if (!(obj instanceof Map)) {
            throw new BizException("message content is not Map");
        }
        Message msg = new Message();
        try {
            //obj中的key,如果bean中有这个属性，就把这个key对应的value值赋给msg的属性
            BeanUtils.populate(msg, (Map<String, ? extends Object>) obj);
        } catch (Throwable e) {
            throw new BizException("message content is incorrect");
        }

        log.info("publish topic:{},payload:{}", msg.getTopic(), msg.getPayload());

        client.publish(msg.getTopic(),
                Buffer.buffer(msg.getPayload()),
                MqttQoS.AT_LEAST_ONCE,
                false,
                false);
    }

    @Override
    public boolean exist(String productKey, String deviceName) {
        return true;

        /*//先作为子设备查找是否存在父设备
        Device device = deviceChildToParent.get(new Device(productKey, deviceName).toString());
        if (device != null) {
            return true;
        }*/

        //return mqttVerticle.exist(productKey, deviceName);
    }


    /**
     * 透传解码
     */
    public ThingModelMessage transparentDecode(Map<String, Object> msg) throws InvocationTargetException, IllegalAccessException {
        TransparentMsg transparentMsg = new TransparentMsg();
        BeanUtils.populate(transparentMsg, msg);
        return transparentConverter.decode(transparentMsg);
    }

    /**
     * 透传编码
     */
    public DeviceMessage transparentEncode(ThingService<?> service, cc.iotkit.converter.Device device) {
        return transparentConverter.encode(service, device);
    }

    public Object getCompMqttClientIdList(){
        String[] result =  compMqttClientIdList.toArray(new String[0]);
        return JsonUtil.toJsonString(result);
    }

    @Data
    public static class Message {
        private String topic;
        private String payload;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Device {
        private String productKey;
        private String deviceName;
    }
}
