package cc.iotkit.comp.mqtt;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comp.AbstractComponent;
import cc.iotkit.comp.CompConfig;
import cc.iotkit.comp.model.DeviceState;
import cc.iotkit.converter.DeviceMessage;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class MqttComponent extends AbstractComponent {

    private Vertx vertx;
    private CountDownLatch countDownLatch;
    private String deployedId;
    private MqttVerticle mqttVerticle;
    private Map<String, Device> deviceChildToParent = new HashMap<>();

    public void create(CompConfig config) {
        super.create(config);
        vertx = Vertx.vertx();
        MqttConfig mqttConfig = JsonUtil.parse(config.getOther(), MqttConfig.class);
        mqttVerticle = new MqttVerticle(mqttConfig);
    }

    public void start() {
        try {
            mqttVerticle.setExecutor(getHandler());
            countDownLatch = new CountDownLatch(1);
            Future<String> future = vertx.deployVerticle(mqttVerticle);
            future.onSuccess((s -> {
                deployedId = s;
                countDownLatch.countDown();
            }));
            future.onFailure((e) -> {
                countDownLatch.countDown();
                log.error("start mqtt component failed", e);
            });
            countDownLatch.await();
            future.succeeded();
        } catch (Throwable e) {
            throw new BizException("start mqtt component error", e);
        }
    }

    @SneakyThrows
    public void stop() {
        mqttVerticle.stop();
        Future<Void> future = vertx.undeploy(deployedId);
        future.onSuccess(unused -> log.info("stop mqtt component success"));
    }

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
            BeanUtils.populate(msg, (Map<String, ? extends Object>) obj);
        } catch (Throwable e) {
            throw new BizException("message content is incorrect");
        }

        mqttVerticle.publish(parent.getProductKey(), parent.getDeviceName(),
                msg.getTopic(), msg.getPayload());
    }

    @Override
    public boolean exist(String productKey, String deviceName) {
        //先作为子设备查找是否存在父设备
        Device device = deviceChildToParent.get(new Device(productKey, deviceName).toString());
        if (device != null) {
            return true;
        }

        return mqttVerticle.exist(productKey, deviceName);
    }

    @Override
    public CompConfig getConfig() {
        return config;
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
