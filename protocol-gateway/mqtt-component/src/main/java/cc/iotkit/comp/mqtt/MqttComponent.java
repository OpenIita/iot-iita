package cc.iotkit.comp.mqtt;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comp.AbstractComponent;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class MqttComponent extends AbstractComponent {

    private Vertx vertx;
    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private String deployedId;
    private MqttConfig mqttConfig;

    public void create(String config) {
        vertx = Vertx.vertx();
        mqttConfig = JsonUtil.parse(config, MqttConfig.class);
    }

    public void start() {
        try {
            Future<String> future = vertx.deployVerticle(new MqttVerticle(mqttConfig, getMessageHandler()));
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

    public void stop() {
        Future<Void> future = vertx.undeploy(deployedId);
        future.onSuccess(unused -> log.info("stop mqtt component success"));
    }

    public void destroy() {
        vertx.close();
    }

}
