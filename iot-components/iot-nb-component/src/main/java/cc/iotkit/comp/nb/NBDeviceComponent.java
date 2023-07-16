/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comp.nb;

import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.thing.ThingService;
import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.comp.AbstractDeviceComponent;
import cc.iotkit.comp.CompConfig;
import cc.iotkit.comp.model.DeviceState;
import cc.iotkit.converter.DeviceMessage;
import cc.iotkit.model.device.message.ThingModelMessage;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class NBDeviceComponent extends AbstractDeviceComponent {

    private Vertx vertx;
    private CountDownLatch countDownLatch;
    private String deployedId;
    private NBVerticle NBVerticle;
    private final Map<String, Device> deviceChildToParent = new HashMap<>();

    @Override
    public void create(CompConfig config) {
        super.create(config);
        vertx = Vertx.vertx();
        NBConfig NBConfig = JsonUtils.parseObject(config.getOther(), NBConfig.class);
        NBVerticle = new NBVerticle(NBConfig);
    }

    public void start() {
        try {
            NBVerticle.setExecutor(getHandler());
            countDownLatch = new CountDownLatch(1);
            Future<String> future = vertx.deployVerticle(NBVerticle);
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
            throw new BizException(ErrCode.COMPONENT_START_ERROR, e);
        }
    }

    @SneakyThrows
    public void stop() {
        NBVerticle.stop();
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
    public DeviceMessage send(DeviceMessage message) {
        Device child = new Device(message.getProductKey(), message.getDeviceName());
        //作为子设备查找父设备
        Device parent = deviceChildToParent.get(child.toString());
        if (parent == null) {
            parent = child;
        }

        Object obj = message.getContent();
        if (!(obj instanceof Map)) {
            throw new BizException(ErrCode.DATA_FORMAT_ERROR);
        }
        Message msg = new Message();
        try {
            BeanUtils.populate(msg, (Map<String, ? extends Object>) obj);
        } catch (Throwable e) {
            throw new BizException(ErrCode.DATA_FORMAT_ERROR);
        }
        log.info("publish topic:{},payload:{}", msg.getTopic(), msg.getPayload());
        NBVerticle.publish(parent.getProductKey(), parent.getDeviceName(),
                msg.getTopic(), msg.getPayload());

        return message;
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
