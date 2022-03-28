package cc.iotkit.comp.emqx;

import cc.iotkit.comp.IMessageHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
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
        Router backendRouter = Router.router(vertx);

        backendRouter.route(HttpMethod.POST, "/mqtt/auth").handler(rc -> {
            JsonObject json = rc.getBodyAsJson();
            try {
                executor.onReceive(new HashMap<>(), "auth", json.toString());
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
                Map<String, Object> head = new HashMap<>();
                head.put("topic", json.getString("topic"));
                executor.onReceive(head, "subscribe", json.toString());
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
