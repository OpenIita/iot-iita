/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.test.mqtt.service;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.test.mqtt.config.Mqtt;
import cc.iotkit.test.mqtt.model.Request;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import io.vertx.mqtt.messages.MqttConnAckMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class Gateway extends Device {

    private List<Device> subDevices = new ArrayList<>();

    private Consumer<Device> deviceOnlineListener;

    private MqttClient client;

    public Gateway(String productKey, String deviceName) {
        super(productKey, deviceName, "GW01");
    }

    @SneakyThrows
    public void start() {
        String clientId = String.format("%s_%s_%s", productKey, deviceName, getModel());

        try {
            MqttClientOptions options = new MqttClientOptions();
            options.setUsername(this.deviceName);
            options.setPassword(DigestUtils.md5Hex(Constants.PRODUCT_SECRET + clientId));
            options.setCleanSession(true);
            options.setKeepAliveInterval(30);
            options.setClientId(clientId);
            options.setReconnectInterval(3000);
            options.setReconnectAttempts(100);

            client = MqttClient.create(Vertxs.getVertx(), options);

            CountDownLatch countDownLatch = new CountDownLatch(1);
            client.connect(Mqtt.brokerPort, Mqtt.brokerHost, s -> {
                if (s.succeeded()) {
                    log.info("mqtt connected,clientId:{}", clientId);
                    countDownLatch.countDown();
                } else {
                    log.info("mqtt connect failed,clientId:{}", clientId);
                }
            });
            countDownLatch.await();

            // 订阅
            String topic = String.format("/sys/%s/%s/c/#", productKey, deviceName);
            log.info("subscribe topic:{}", topic);

            client.subscribe(topic, 1, r -> {
                //注册子设备
                for (Device subDevice : subDevices) {
                    log.info("start register sub device,pk:{},dn:{}", subDevice.getProductKey(), subDevice.getDeviceName());
                    Request request = new Request();
                    request.setId(UUID.randomUUID().toString());
                    request.setParams(subDevice);
                    String registerTopic = String.format("/sys/%s/%s/s/register", productKey, deviceName);
                    String payload = JsonUtil.toJsonString(request);
                    client.publish(registerTopic, Buffer.buffer(payload), MqttQoS.AT_LEAST_ONCE, false, false);
                    log.info("publish message,topic:{},payload:{}", registerTopic, payload);
                }
            });

            client.publishHandler(new MessageHandler(client, this, deviceOnlineListener));

            client.closeHandler((v) -> {
                log.info("{} closed", deviceName);
            });

        } catch (Throwable e) {
            log.error("connect mqtt-broker error", e);
        }
    }

    public void addSubDevice(String productKey, String deviceName, String model) {
        subDevices.add(new Device(productKey, deviceName, model));
    }

    public void onDeviceOnline(Consumer<Device> listener) {
        this.deviceOnlineListener = listener;
    }


    public static class OnConnected implements Handler<AsyncResult<MqttConnAckMessage>> {

        @Override
        public void handle(AsyncResult<MqttConnAckMessage> mqttConnAckMessageAsyncResult) {

        }
    }

}
