package cc.iotkit.comp.mqtt;

import cc.iotkit.comp.IMessageHandler;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttProperties;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.mqtt.MqttAuth;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;
import io.vertx.mqtt.MqttTopicSubscription;
import io.vertx.mqtt.messages.codes.MqttSubAckReasonCode;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MqttVerticle extends AbstractVerticle {

    private MqttServer mqttServer;

    private final MqttConfig config;

    private final IMessageHandler executor;

    public MqttVerticle(MqttConfig config, IMessageHandler executor) {
        this.config = config;
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

            String authJson = auth.toJson()
                    .put("clientid", endpoint.clientIdentifier()).toString();

            log.info("MQTT client auth,username:{},password:{}", auth.getUsername(), auth.getPassword());
            try {
                executor.onReceive(new HashMap<>(), "auth", authJson);
            } catch (Throwable e) {
                log.error("auth failed", e);
                endpoint.reject(MqttConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED);
            }

            log.info("MQTT client keep alive timeout = {} ", endpoint.keepAliveTimeSeconds());

            endpoint.accept(false);
            endpoint.disconnectMessageHandler(disconnectMessage -> {
                log.info("Received disconnect from client, reason code = {}", disconnectMessage.code());
                executor.onReceive(new HashMap<>(), "disconnect", authJson);
            }).subscribeHandler(subscribe -> {
                List<MqttSubAckReasonCode> reasonCodes = new ArrayList<>();
                for (MqttTopicSubscription s : subscribe.topicSubscriptions()) {
                    log.info("Subscription for {},with QoS {}", s.topicName(), s.qualityOfService());
                    try {
                        executor.onReceive(new HashMap<>(), "subscribe", s.topicName());
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
                        executor.onReceive(new HashMap<>(), "unsubscribe", t);
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
                try {
                    Map<String, Object> head = new HashMap<>();
                    head.put("topic", message.topicName());
                    executor.onReceive(head, "", payload);
                } catch (Throwable e) {
                    log.error("handler message failed,topic:" + message.topicName(), e);
                }

                if (message.qosLevel() == MqttQoS.AT_LEAST_ONCE) {
                    endpoint.publishAcknowledge(message.messageId());
                } else if (message.qosLevel() == MqttQoS.EXACTLY_ONCE) {
                    endpoint.publishReceived(message.messageId());
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
        mqttServer.close(voidAsyncResult -> log.info("close mqtt server..."));
    }
}
