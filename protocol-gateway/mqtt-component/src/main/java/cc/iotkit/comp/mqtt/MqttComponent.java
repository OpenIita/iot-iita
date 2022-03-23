package cc.iotkit.comp.mqtt;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comp.AbstractComponent;
import cc.iotkit.comp.IMessageHandler;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class MqttComponent extends AbstractComponent {

    private Vertx vertx;
    private CountDownLatch countDownLatch;
    private String deployedId;
    private MqttVerticle mqttVerticle;

    public void create(String config) {
        vertx = Vertx.vertx();
        MqttConfig mqttConfig = JsonUtil.parse(config, MqttConfig.class);
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

}
