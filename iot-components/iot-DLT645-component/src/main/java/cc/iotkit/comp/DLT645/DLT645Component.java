/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comp.DLT645;

import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.comp.AbstractDeviceComponent;
import cc.iotkit.comp.CompConfig;
import cc.iotkit.converter.DeviceMessage;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Data
@Slf4j
public class DLT645Component extends AbstractDeviceComponent {

    private Vertx vertx;

    private CountDownLatch countDownLatch;

    private DLT645Verticle DLT645Verticle;

    private String deployedId;

    private String id;

    @Override
    public void create(CompConfig config) {
        super.create(config);
        vertx = Vertx.vertx();
        this.id = UUID.randomUUID().toString();
        DLT645Config DLT645Config = JsonUtils.parseObject(config.getOther(), DLT645Config.class);
        DLT645Verticle = new DLT645Verticle(DLT645Config);
    }

    @Override
    public void start() {
        try {
            DLT645Verticle.setExecutor(getHandler());
            countDownLatch = new CountDownLatch(1);
            Future<String> future = vertx.deployVerticle(DLT645Verticle);
            future.onSuccess((s -> {
                deployedId = s;
                countDownLatch.countDown();
            }));
            future.onFailure(e -> {
                countDownLatch.countDown();
                log.error("start GLT645 component failed", e);
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
        DLT645Verticle.stop();
        Future<Void> future = vertx.undeploy(deployedId);
        future.onSuccess(unused -> log.info("stop GLT645 component success"));
    }

    @Override
    public void destroy() {
    }


    @Override
    public DeviceMessage send(DeviceMessage message) {
        DLT645Verticle.sendMsg(message);
        return message;
    }
}
