/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comp.emqx;

import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.thing.ThingService;
import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.common.utils.SpringUtils;
import cc.iotkit.common.utils.ThreadUtil;
import cc.iotkit.comp.AbstractDeviceComponent;
import cc.iotkit.comp.CompConfig;
import cc.iotkit.comp.IMessageHandler;
import cc.iotkit.comp.model.DeviceState;

import cc.iotkit.converter.DeviceMessage;
import cc.iotkit.data.manager.IDeviceInfoData;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.ThingModelMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import lombok.*;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class EmqxDeviceComponent extends AbstractDeviceComponent implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(EmqxDeviceComponent.class);
    private Vertx vertx;
    private AuthVerticle authVerticle;
    private CountDownLatch countDownLatch;
    private String deployedId;
    private EmqxConfig mqttConfig;
    private MqttClient client;
    private boolean mqttConnected = false;
    private final ScheduledThreadPoolExecutor emqxConnectTask = ThreadUtil.newScheduled(1, "emqx_connect");

    /**
     * 组件mqtt clientId，默认通过mqtt auth / acl验证。
     */
    private final Set<String> compMqttClientIdList = new HashSet<>();

    private final TransparentConverter transparentConverter = new TransparentConverter();

    @Override
    public void create(CompConfig config) {
        super.create(config);
        vertx = Vertx.vertx();
        mqttConfig = JsonUtils.parseObject(config.getOther(), EmqxConfig.class);
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
                log.info("start emqx auth component success");
            }));
            future.onFailure((e) -> {
                countDownLatch.countDown();
                log.error("start emqx auth component failed", e);
            });
            countDownLatch.await();

            emqxConnectTask.scheduleWithFixedDelay(this, 0, 3, TimeUnit.SECONDS);
        } catch (Throwable e) {
            throw new BizException(ErrCode.COMPONENT_START_ERROR, e);
        }
    }

    @Override
    public void run() {
        try {
            if (mqttConnected) {
                return;
            }

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

            List<String> topics = mqttConfig.getSubscribeTopics();
            Map<String, Integer> subscribes = new HashMap<>();

            for (String topic : topics) {
                subscribes.put(topic, 1);
            }

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
                    mqttConnected = true;
                    /*
                     * 订阅主题：
                     * /sys/+/+/s/#
                     * /sys/client/connected
                     * /sys/client/disconnected
                     * /sys/session/subscribed
                     * /sys/session/unsubscribed
                     */
                    client.subscribe(subscribes, e -> {
                        if (e.succeeded()) {
                            log.info("===>subscribe success: {}", e.result());
                        } else {
                            log.error("===>subscribe fail: ", e.cause());
                        }
                    });

                } else {
                    mqttConnected = false;
                    log.error("client connect fail: ", s.cause());
                }
            }).exceptionHandler(event -> log.error("client fail", event));

        } catch (Throwable e) {
            throw new BizException(ErrCode.COMPONENT_START_ERROR, e);
        }
    }

    @SneakyThrows
    @Override
    public void stop() {
        authVerticle.stop();
        Future<Void> future = vertx.undeploy(deployedId);
        future.onSuccess(unused -> log.info("stop emqx auth component success"));

        client.disconnect()
                .onSuccess(unused -> {
                    mqttConnected = false;
                    log.info("stop emqx component success");
                })
                .onFailure(unused -> log.info("stop emqx component failure"));

        emqxConnectTask.shutdown();
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
        IDeviceInfoData deviceInfoService = SpringUtils.getBean("deviceInfoDataCache");

        DeviceInfo deviceInfo = deviceInfoService.findByProductKeyAndDeviceName(state.getProductKey(), state.getDeviceName());
        if (deviceInfo != null) {
            boolean isOnline = DeviceState.STATE_ONLINE.equals(state.getState());
            deviceInfo.getState().setOnline(isOnline);
            if (!isOnline) {
                deviceInfo.getState().setOfflineTime(System.currentTimeMillis());
            }
            if (isOnline) {
                deviceInfo.getState().setOnlineTime(System.currentTimeMillis());
            }
            deviceInfoService.save(deviceInfo);
        }
    }

    @Override
    public DeviceMessage send(DeviceMessage message) {
        Object obj = message.getContent();
        if (!(obj instanceof Map)) {
            throw new BizException(ErrCode.DATA_FORMAT_ERROR);
        }
        Message msg = new Message();
        try {
            //obj中的key,如果bean中有这个属性，就把这个key对应的value值赋给msg的属性
            BeanUtils.populate(msg, (Map<String, ? extends Object>) obj);
        } catch (Throwable e) {
            throw new BizException(ErrCode.DATA_FORMAT_ERROR);
        }

        log.info("publish topic:{},payload:{}", msg.getTopic(), msg.getPayload());

        client.publish(msg.getTopic(),
                Buffer.buffer(msg.getPayload()),
                MqttQoS.AT_LEAST_ONCE,
                false,
                false);

        return message;
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

    /**
     * 提供js调用
     */
    public Object getCompMqttClientIdList() {
        String[] result = compMqttClientIdList.toArray(new String[0]);
        return JsonUtils.toJsonString(result);
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
