/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comp.biz;

import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.comp.CompConfig;
import cc.iotkit.comp.IComponent;
import cc.iotkit.script.IScriptEngine;
import cc.iotkit.script.ScriptEngineFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Data
@Slf4j
public class HttpBizComponent implements IComponent {

    private final Vertx vertx = Vertx.vertx();

    private final IScriptEngine scriptEngine = ScriptEngineFactory.getScriptEngine("js");

    private Object scriptObj;

    private CompConfig config;

    private HttpConfig httpConfig;

    private String script;

    private HttpServer backendServer;

    private String id;

    @Override
    public void create(CompConfig config) {
        this.id = UUID.randomUUID().toString();
        this.httpConfig = JsonUtils.parseObject(config.getOther(), HttpConfig.class);
        scriptEngine.setScript(script);
    }

    @Override
    public void start() {
        backendServer = vertx.createHttpServer();
        Router backendRouter = Router.router(vertx);
        backendRouter.route().handler(BodyHandler.create())
                .handler(rc -> {
                    try {
                        Map<String, Object> httpHeader = getData(rc.request().headers());
                        log.info("request header:{}", JsonUtils.toJsonString(httpHeader));
                        Map<String, List<Object>> httpParams = getListData(rc.request().params());
                        log.info("request params:{}", JsonUtils.toJsonString(httpParams));

                        HttpServerRequest httpRequest = rc.request();
                        String contentType = httpRequest.headers().get("Content-Type");
                        Map<String, Object> responseHeader = new HashMap<>();
                        if ("application/json".equals(contentType)) {
                            String bodyStr = rc.getBody().toString();
                            Map body = JsonUtils.parseObject(bodyStr, Map.class);
                            log.info("request body:{}", bodyStr);

                            String response;
                            try {
                                HttpContent content =
                                        scriptEngine.invokeMethod(
                                                new TypeReference<>() {
                                                },
                                                "onReceive",
                                                httpRequest.method().name(),
                                                httpRequest.path(),
                                                httpHeader,
                                                httpParams,
                                                body);
                                responseHeader = content.getHeader();
                                response = content.getContent();
                                response = response == null ? "" : response;
                            } catch (Throwable e) {
                                log.error("invokeMethod onReceive error", e);
                                response = e.getMessage();
                            }

                            HttpServerResponse httpServerResponse = rc.response();
                            //设置响应头
                            responseHeader.forEach((key, value) -> {
                                //大写转换
                                key = key.replaceAll("([A-Z])", "-$1").toLowerCase();
                                httpServerResponse.putHeader(key, value.toString());
                            });

                            log.info("response,header:{},content:{}", responseHeader, response);
                            //设置响应内容
                            httpServerResponse
                                    .end(response);
                        } else {
                            rc.response().end("");
                        }
                    } catch (Throwable e) {
                        log.error("handle request error", e);
                        rc.response().end("server error:" + e.getMessage());
                    }
                });

        backendServer.requestHandler(backendRouter)
                .listen(httpConfig.getPort(), http -> {
                    if (http.succeeded()) {
                        log.info("http server create succeed,port:{}", httpConfig.getPort());
                    } else {
                        log.error("http server create failed", http.cause());
                    }
                });
    }

    @Override
    public void putScriptEnv(String key, Object value) {
        scriptEngine.putScriptEnv(key, value);
    }

    @Override
    public void stop() {
        backendServer.close();
    }

    @Override
    public void destroy() {
    }

    private static Map<String, List<Object>> getListData(MultiMap multiMap) {
        Map<String, List<Object>> listData = new HashMap<>();
        for (Map.Entry<String, String> entry : multiMap.entries()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            listData.putIfAbsent(key, new ArrayList<>());
            listData.get(key).add(value);
        }
        return listData;
    }

    private static Map<String, Object> getData(MultiMap multiMap) {
        Map<String, Object> data = new HashMap<>();
        for (Map.Entry<String, String> entry : multiMap.entries()) {
            data.put(entry.getKey(), entry.getValue());
        }
        return data;
    }

}
