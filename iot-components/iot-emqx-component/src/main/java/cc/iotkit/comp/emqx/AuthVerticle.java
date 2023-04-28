/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comp.emqx;

import cc.iotkit.comp.IMessageHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthVerticle extends AbstractVerticle {

    private HttpServer backendServer;

    private IMessageHandler executor;

    private final EmqxConfig config;

    public void setExecutor(IMessageHandler executor) {
        this.executor = executor;
    }

    public AuthVerticle(EmqxConfig config) {
        this.config = config;
    }

    @Override
    public void start() {
        backendServer = vertx.createHttpServer();

        //第一步 声明Router&初始化Router
        Router backendRouter = Router.router(vertx);
        //获取body参数，得先添加这句
        backendRouter.route().handler(BodyHandler.create());

        //第二步 配置Router解析url
        backendRouter.route(HttpMethod.POST, "/mqtt/auth").handler(rc -> {
            String json = rc.getBodyAsString();
            log.info("mqtt auth:{}", json);
            try {
                Map<String, Object> head = new HashMap<>();
                head.put("topic", "/mqtt/auth");
                executor.onReceive(head, "auth", json);
                httpResult(rc.response(), 200);
            } catch (Throwable e) {
                httpResult(rc.response(), 500);
                log.error("mqtt auth failed", e);
            }
        });
        backendRouter.route(HttpMethod.POST, "/mqtt/acl").handler(rc -> {
            String json = rc.getBodyAsString();
            log.info("mqtt acl:{}", json);
            try {
                Map<String, Object> head = new HashMap<>();
                head.put("topic", "/mqtt/acl");
                executor.onReceive(head, "acl", json);
                httpResult(rc.response(), 200);
            } catch (Throwable e) {
                httpResult(rc.response(), 500);
                log.error("mqtt acl failed", e);
            }
        });

        backendServer.requestHandler(backendRouter).listen(config.getAuthPort());
    }

    private void httpResult(HttpServerResponse response, int code) {
        response.putHeader("Content-Type", "application/json");
        response
                .setStatusCode(code);
        response
                .end("{\"result\": \"" + (code == 200 ? "allow" : "deny") + "\"}");
    }

    @Override
    public void stop() throws Exception {
        backendServer.close(voidAsyncResult -> log.info("close emqx auth server..."));
    }
}
