/*
 *
 *  * | Licensed 未经许可不能去掉「OPENIITA」相关版权
 *  * +----------------------------------------------------------------------
 *  * | Author: xw2sy@163.com
 *  * +----------------------------------------------------------------------
 *
 *  Copyright [2024] [OPENIITA]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */

package cc.iotkit.screen.staticres;

import cc.iotkit.screen.api.ScreenApiHandle;
import cc.iotkit.screen.config.ScreenConfig;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author：tfd
 * @Date：2023/6/25 16:03
 */
@Slf4j
public class ScreenVerticle extends AbstractVerticle {

    private HttpServer httpServer;

    private int port;

    private String packageName;

    private ScreenApiHandle apiHandler;

    private ScreenConfig screenConfig;

    public ScreenVerticle(int port,String packageName,ScreenConfig screenConfig) {
        this.port = port;
        this.packageName = packageName;
        this.screenConfig = screenConfig;
    }

    public void setApiHandler(ScreenApiHandle apiHandler) {
        this.apiHandler = apiHandler;
    }

    @Override
    public void start() {
        httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route(screenConfig.screenAdmin + "/*").handler(StaticHandler.create(screenConfig.getScreenDir() + "/" + apiHandler.getScreenId() + "/" + packageName));
        router.get(screenConfig.screenAdmin).handler(ctx -> {
            ctx.response().sendFile(screenConfig.getScreenDir() + "/" + apiHandler.getScreenId() + "/" + packageName + "/index.html");
        });
        router.get("/*").handler(ctx -> {
            String res = apiHandler.httpReq(ctx.request(), ctx.response());
            ctx.response().end(res);
        });
        router.post("/*").handler(BodyHandler.create()).handler(ctx -> {
            String res = apiHandler.httpReq(ctx.request(), ctx.response());
            ctx.response().end(res);
        });
        httpServer.requestHandler(router).listen(port, (http) -> {
            if (http.succeeded()) {
                log.info("screen server create succeed,port:{}", port);
            } else {
                log.error("screen server create failed", http.cause());
            }
        });
    }

    @Override
    public void stop() {
        httpServer.close(voidAsyncResult -> log.info("close screen server..."));
    }
}
