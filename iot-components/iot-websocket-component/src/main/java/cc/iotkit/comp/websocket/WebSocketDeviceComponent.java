package cc.iotkit.comp.websocket;

import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comp.AbstractDeviceComponent;
import cc.iotkit.comp.CompConfig;
import cc.iotkit.comp.model.DeviceState;
import cc.iotkit.comp.utils.SpringUtils;
import cc.iotkit.comp.websocket.client.WebSocketClientVerticle;
import cc.iotkit.comp.websocket.server.WebSocketServerVerticle;
import cc.iotkit.converter.DeviceMessage;
import cc.iotkit.data.IDeviceInfoData;
import cc.iotkit.model.device.DeviceInfo;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
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
            throw new BizException(ErrCode.COMPONENT_START_ERROR, e);
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
        IDeviceInfoData deviceInfoService = SpringUtils.getBean("deviceInfoDataCache");
        DeviceInfo parentDevice=deviceInfoService.findByProductKeyAndDeviceName(state.getProductKey(),state.getDeviceName());
        List<DeviceInfo> childDevices=deviceInfoService.findByParentId(parentDevice.getId());
        if(childDevices!=null&&childDevices.size()>0){//说明是父设备下面有子设备
            if (DeviceState.STATE_ONLINE.equals(state.getState())) {
                //保存子设备所属父设备
                for (DeviceInfo childDevice:childDevices) {
                    deviceChildToParent.put(childDevice.getProductKey()+childDevice.getDeviceName(),
                            new Device(parentDevice.getProductKey(), parentDevice.getDeviceName())
                    );
                }
            } else {
                //删除关系
                for (DeviceInfo childDevice:childDevices) {
                    deviceChildToParent.remove(childDevice.getProductKey()+childDevice.getDeviceName());
                }

            }
        }
    }

    @Override
    public DeviceMessage send(DeviceMessage message) {
        Device child = new Device(message.getProductKey(), message.getDeviceName());
        for (String key:deviceChildToParent.keySet()) {
            log.info("deviceChildToParent key：", key);
        }
        //作为子设备查找父设备
        Device parent = deviceChildToParent.get(message.getProductKey()+message.getDeviceName());
        if (parent == null) {
            parent = child;
        }
        message.setProductKey(parent.getProductKey());
        message.setDeviceName(parent.getDeviceName());
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
