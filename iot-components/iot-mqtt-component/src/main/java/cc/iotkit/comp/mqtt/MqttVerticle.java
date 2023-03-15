/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comp.mqtt;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.comp.IMessageHandler;
import cc.iotkit.comp.model.ReceiveResult;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttProperties;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.mqtt.*;
import io.vertx.mqtt.messages.codes.MqttSubAckReasonCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MqttVerticle extends AbstractVerticle {

    private MqttServer mqttServer;

    private final MqttConfig config;

    private IMessageHandler executor;

    private final Map<String, MqttEndpoint> endpointMap = new HashMap<>();

    // 增加一个客户端连接clientid-连接状态池，避免mqtt关闭的时候走异常断开和mqtt断开的handler，导致多次离线消息
    private static final Map<String, Boolean> mqttConnectPool = new ConcurrentHashMap<>();

    public MqttVerticle(MqttConfig config) {
        this.config = config;
    }

    public void setExecutor(IMessageHandler executor) {
        this.executor = executor;
    }

    @Override
    public void start() throws Exception {
        MqttServerOptions options = new MqttServerOptions()
                .setPort(config.getPort());
        if (config.isSsl()) {
            options = options.setSsl(true)
                    .setKeyCertOptions(new PemKeyCertOptions()
                            .setKeyPath(config.getSslKey())
                            .setCertPath(config.getSslCert()));
        }

        mqttServer = MqttServer.create(vertx, options);
        mqttServer.endpointHandler(endpoint -> {
            log.info("MQTT client:{} request to connect, clean session = {}", endpoint.clientIdentifier(), endpoint.isCleanSession());

            MqttAuth auth = endpoint.auth();
            if (auth == null) {
                return;
            }

            String clientId = endpoint.clientIdentifier();
            String authJson = auth.toJson()
                    .put("clientid", clientId).toString();

            log.info("MQTT client auth,clientId:{},username:{},password:{}",
                    clientId, auth.getUsername(), auth.getPassword());
            try {
                executor.onReceive(new HashMap<>(), "auth", authJson, (r) -> {
                    if (r == null) {
                        //认证失败
                        endpoint.reject(MqttConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED);
                        return;
                    }
                    //保存设备与连接关系
                    endpointMap.put(getEndpointKey(r), endpoint);
                    mqttConnectPool.put(clientId, true);
                });
            } catch (Throwable e) {
                log.error("auth failed", e);
                endpoint.reject(MqttConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED);
                return;
            }

            log.info("MQTT client keep alive timeout = {} ", endpoint.keepAliveTimeSeconds());

            endpoint.accept(false);
            endpoint.closeHandler((v) -> {
                log.warn("client connection closed,clientId:{}", clientId);
                if (mqttConnectPool.get(clientId) == false) return;
                executor.onReceive(new HashMap<>(), "disconnect", clientId, (r) -> {
                    //删除设备与连接关系
                    endpointMap.remove(getEndpointKey(r));
                });
            }).disconnectMessageHandler(disconnectMessage -> {
                log.info("Received disconnect from client, reason code = {}", disconnectMessage.code());
                executor.onReceive(new HashMap<>(), "disconnect", clientId, (r) -> {
                    //删除设备与连接关系
                    endpointMap.remove(getEndpointKey(r));
                    mqttConnectPool.put(clientId, false);
                });
            }).subscribeHandler(subscribe -> {
                List<MqttSubAckReasonCode> reasonCodes = new ArrayList<>();
                for (MqttTopicSubscription s : subscribe.topicSubscriptions()) {
                    log.info("Subscription for {},with QoS {}", s.topicName(), s.qualityOfService());
                    try {
                        Map<String, Object> head = new HashMap<>();
                        head.put("topic", s.topicName());
                        executor.onReceive(head, "subscribe", clientId);
                        reasonCodes.add(MqttSubAckReasonCode.qosGranted(s.qualityOfService()));
                    } catch (Throwable e) {
                        log.error("subscribe failed,topic:" + s.topicName(), e);
                        reasonCodes.add(MqttSubAckReasonCode.NOT_AUTHORIZED);
                    }
                }
                // ack the subscriptions request
                endpoint.subscribeAcknowledge(subscribe.messageId(), reasonCodes, MqttProperties.NO_PROPERTIES);

            }).unsubscribeHandler(unsubscribe -> {
                for (String t : unsubscribe.topics()) {
                    log.info("Unsubscription for {}", t);
                    try {
                        Map<String, Object> head = new HashMap<>();
                        head.put("topic", t);
                        executor.onReceive(head, "unsubscribe", clientId);
                    } catch (Throwable e) {
                        log.error("unsubscribe failed,topic:" + t, e);
                    }
                }
                // ack the subscriptions request
                endpoint.unsubscribeAcknowledge(unsubscribe.messageId());
            }).publishHandler(message -> {
                String payload = message.payload().toString(Charset.defaultCharset());
                log.info("Received message:{}, with QoS {}", payload,
                        message.qosLevel());
                if (StringUtils.isBlank(payload)) {
                    return;
                }

                try {
                    Map<String, Object> head = new HashMap<>();
                    head.put("topic", message.topicName());
                    executor.onReceive(head, "", payload);
                    if (message.qosLevel() == MqttQoS.AT_LEAST_ONCE) {
                        endpoint.publishAcknowledge(message.messageId());
                    } else if (message.qosLevel() == MqttQoS.EXACTLY_ONCE) {
                        endpoint.publishReceived(message.messageId());
                    }
                } catch (Throwable e) {
                    log.error("handler message failed,topic:" + message.topicName(), e);
                }
            }).publishReleaseHandler(endpoint::publishComplete);
        }).listen(ar -> {
            if (ar.succeeded()) {
                log.info("MQTT server is listening on port " + ar.result().actualPort());
            } else {
                log.error("Error on starting the server", ar.cause());
            }
        });
    }

    @Override
    public void stop() throws Exception {
        for (MqttEndpoint endpoint : endpointMap.values()) {
            executor.onReceive(new HashMap<>(), "disconnect", endpoint.clientIdentifier());
        }
        mqttServer.close(voidAsyncResult -> log.info("close mqtt server..."));
    }

    private String getEndpointKey(ReceiveResult result) {
        return getEndpointKey(result.getProductKey(), result.getDeviceName());
    }

    private String getEndpointKey(String productKey, String deviceName) {
        return String.format("%s_%s", productKey, deviceName);
    }

    public boolean exist(String productKey, String deviceName) {
        return endpointMap.containsKey(getEndpointKey(productKey, deviceName));
    }

    public void publish(String productKey, String deviceName, String topic, String msg) {
        MqttEndpoint endpoint = endpointMap.get(getEndpointKey(productKey, deviceName));
        if (endpoint == null) {
            throw new BizException("endpoint does not exist");
        }
        Future<Integer> result = endpoint.publish(topic, Buffer.buffer(msg),
                MqttQoS.AT_LEAST_ONCE, false, false);
        result.onFailure(e -> log.error("public topic failed", e));
        result.onSuccess(integer -> {
            log.info("publish success,topic:{},payload:{}", topic, msg);
        });
    }
}
