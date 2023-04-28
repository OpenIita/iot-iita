package cc.iotkit.comp.websocket;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comp.AbstractDeviceComponent;
import cc.iotkit.comp.CompConfig;
import cc.iotkit.comp.model.DeviceState;
import cc.iotkit.comp.websocket.client.WebSocketClientVerticle;
import cc.iotkit.comp.websocket.server.WebSocketServerVerticle;
import cc.iotkit.converter.DeviceMessage;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class WebSocketDeviceComponent extends AbstractDeviceComponent {

    private Vertx vertx;
    private CountDownLatch countDownLatch;
    private String deployedId;
    private AbstractDeviceVerticle webSocketVerticle;
    private final Map<String, Device> deviceChildToParent = new HashMap<>();

    @Override
    public void create(CompConfig config) {
        super.create(config);
        vertx = Vertx.vertx();
        String type = JsonUtil.parse(config.getOther(), Map.class).get("type").toString();
        if(AbstractDeviceVerticle.TYPE_CLIENT.equals(type)){
            webSocketVerticle = new WebSocketClientVerticle(config.getOther());
        }else{
            webSocketVerticle = new WebSocketServerVerticle(config.getOther());
        }
    }

    @Override
    public void start() {
        try {
            webSocketVerticle.setExecutor(getHandler());
            countDownLatch = new CountDownLatch(1);
            Future<String> future = vertx.deployVerticle(webSocketVerticle);
            future.onSuccess((s -> {
                deployedId = s;
                countDownLatch.countDown();
            }));
            future.onFailure((e) -> {
                countDownLatch.countDown();
                log.error("start websocket component failed", e);
            });
            countDownLatch.await();
            future.succeeded();
        } catch (Throwable e) {
            throw new BizException("start websocket component error", e);
        }
    }

    @Override
    @SneakyThrows
    public void stop() {
        webSocketVerticle.stop();
        Future<Void> future = vertx.undeploy(deployedId);
        future.onSuccess(unused -> log.info("stop websocket component success"));
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
    public DeviceMessage send(DeviceMessage message) {
        webSocketVerticle.send(message);
        return message;
    }

    @Override
    public CompConfig getConfig() {
        return config;
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
