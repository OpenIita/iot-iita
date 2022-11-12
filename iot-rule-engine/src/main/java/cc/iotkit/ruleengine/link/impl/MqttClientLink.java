package cc.iotkit.ruleengine.link.impl;

import cc.iotkit.common.utils.FIUtil;
import cc.iotkit.ruleengine.link.BaseSinkLink;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * @author huangwenl
 * @date 2022-11-10
 */
public class MqttClientLink implements BaseSinkLink {
    public static final String LINK_TYPE = "mqtt";
    public static final String TOPIC = "topic";
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String PAYLOAD = "payload";

    private MqttClient mqttClient;
    private Consumer<Void> closeHandler;


    @Override
    public boolean open(Map<String, Object> config) {
        try {
            AtomicReference<Vertx> vertx = new AtomicReference<>();
            FIUtil.isTotF(Vertx.currentContext() == null).handler(
                    () -> vertx.set(Vertx.vertx()),
                    () -> vertx.set(Vertx.currentContext().owner())
            );
            MqttClientOptions clientOptions = new MqttClientOptions();
            clientOptions.setUsername((String) config.get(USERNAME));
            clientOptions.setPassword((String) config.get(PASSWORD));
            mqttClient = MqttClient.create(vertx.get(), clientOptions);
            mqttClient.connect((int) config.get(PORT), (String) config.get(HOST));
            mqttClient.closeHandler(Void -> closeHandler.accept(null));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void send(Map<String, Object> msg, Consumer<String> consumer) {
        FIUtil.isTotF(mqttClient.isConnected()).handler(
                () -> {
                    mqttClient.publish((String) msg.get(TOPIC),
                            Buffer.buffer(msg.get(PAYLOAD).toString()),
                            MqttQoS.AT_MOST_ONCE, false, false);
                    consumer.accept(String.format("mqtt, topic:[%s],发送成功:,%s", msg.get(TOPIC), msg.get(PAYLOAD).toString()));
                },
                () -> consumer.accept("mqtt,连接断开,发送失败"));
    }

    @Override
    public void close() {
        mqttClient.disconnect();
        mqttClient = null;
    }

    @Override
    public void closeHandler(Consumer<Void> consumer) {
        this.closeHandler = consumer;
    }
}
