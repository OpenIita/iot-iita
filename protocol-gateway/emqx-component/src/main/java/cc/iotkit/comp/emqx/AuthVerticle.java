package cc.iotkit.comp.emqx;

import cc.iotkit.common.Constants;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comp.IMessageHandler;
import cc.iotkit.comp.utils.SpringUtils;
import cc.iotkit.dao.DeviceRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthVerticle extends AbstractVerticle {

    private HttpServer backendServer;

    private IMessageHandler executor;

    private EmqxConfig config;

    public void setExecutor(IMessageHandler executor) {
        this.executor = executor;
    }

    public AuthVerticle(EmqxConfig config) {
        this.config = config;
    }

    @Override
    public void start() throws Exception {
        backendServer = vertx.createHttpServer();

        //第一步 声明Router&初始化Router
        Router backendRouter = Router.router(vertx);
        //获取body参数，得先添加这句
        backendRouter.route().handler(BodyHandler.create());

        //第二步 配置Router解析url
        backendRouter.route(HttpMethod.POST, "/mqtt/auth").handler(rc -> {
            JsonObject json = rc.getBodyAsJson();

            String clientid = json.getString("clientid", "");
            String username = json.getString("username", "");
            String password = json.getString("password", "");

            log.info(String.format("clientid: %s, username: %s, password: %s", clientid, username, password));

            try {

                //executor.onReceive(new HashMap<>(), "auth", json.toString());
                rc.response().setStatusCode(200)
                        .end();
            } catch (Throwable e) {
                rc.response().setStatusCode(500)
                        .end();
                log.error("mqtt auth failed", e);
            }
        });
        backendRouter.route(HttpMethod.POST, "/mqtt/acl").handler(rc -> {
            JsonObject json = rc.getBodyAsJson();
            try {
                String clientid = json.getString("clientid", "");
                String topic = json.getString("topic", "");
                String access = json.getString("access", "").equals("1") ? "subscribe" : "publish"; //1 - subscribe, 2 - publish

                log.info(String.format("clientid: %s, username: %s, password: %s", clientid, topic, access));


                Map<String, Object> head = new HashMap<>();
                head.put("topic", topic);

                /**
                 * 1、匹配clientId, 匹配topic (topic白名单)
                 */
                if (topic.matches(Constants.MQTT.DEVICE_SUBSCRIBE_TOPIC)) {
                    DeviceRepository deviceRepository = SpringUtils.getBean(DeviceRepository.class);

                    String dd = JsonUtil.toJsonString(deviceRepository.findAll().get(0));
                    log.info(dd);

                    executor.onReceive(head, access, json.toString());
                }


                rc.response().setStatusCode(200)
                        .end();
            } catch (Throwable e) {
                rc.response().setStatusCode(500)
                        .end();
                log.error("mqtt acl failed", e);
            }
        });

        backendServer.requestHandler(backendRouter).listen(config.getAuthPort());
    }

    @Override
    public void stop() throws Exception {
        backendServer.close(voidAsyncResult -> log.info("close emqx auth server..."));
    }
}
