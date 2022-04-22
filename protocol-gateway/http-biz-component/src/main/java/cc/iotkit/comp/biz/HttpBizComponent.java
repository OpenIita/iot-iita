package cc.iotkit.comp.biz;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comp.CompConfig;
import cc.iotkit.comp.IComponent;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class HttpBizComponent implements IComponent {

    private final Vertx vertx = Vertx.vertx();

    private final NashornScriptEngine engine = (NashornScriptEngine) (new ScriptEngineManager()).getEngineByName("nashorn");

    private Object scriptObj;

    private HttpConfig config;

    private String script;

    private HttpServer backendServer;

    @Override
    public void create(CompConfig config) {
        this.config = JsonUtil.parse(config.getOther(), HttpConfig.class);
        try {
            scriptObj = engine.eval(String.format("new (function () {\n%s})()", script));
        } catch (ScriptException e) {
            log.error("init script error", e);
        }
    }

    @Override
    public void start() {
        backendServer = vertx.createHttpServer();
        Router backendRouter = Router.router(vertx);
        backendRouter.route().handler(BodyHandler.create())
                .handler(rc -> {
                    Map<String, Object> httpHeader = getData(rc.request().headers());
                    log.info("request header:{}", JsonUtil.toJsonString(httpHeader));
                    Map<String, List<Object>> httpParams = getListData(rc.request().params());
                    log.info("request params:{}", JsonUtil.toJsonString(httpParams));

                    String contentType = rc.request().headers().get("Content-Type");
                    JsonObject responseHeader = new JsonObject();
                    if ("application/json".equals(contentType)) {
                        String bodyStr = rc.toString();
                        Map body = JsonUtil.parse(bodyStr, Map.class);
                        log.info("request body:{}", bodyStr);

                        String response = "unknown error";
                        String name = "onReceive";
                        if (((ScriptObjectMirror) scriptObj).get(name) != null) {
                            try {
                                Object result = engine.invokeMethod(scriptObj, name, body);
                                Object resultObj = JsonUtil.toObject((ScriptObjectMirror) result);
                                if (resultObj instanceof Map) {
                                    JsonObject data = JsonObject.mapFrom(resultObj);
                                    responseHeader = data.getJsonObject("header");
                                    response = data.getString("content");
                                }
                            } catch (Throwable e) {
                                log.error("invokeMethod onReceive error", e);
                                response = e.getMessage();
                            }
                        } else {
                            log.error("required [onReceive] method");
                        }

                        HttpServerResponse httpServerResponse = rc.response();
                        //设置响应头
                        responseHeader.getMap().forEach((key, value) -> {
                            //大写转换
                            key = key.replaceAll("([A-Z])", "-$1").toLowerCase();
                            httpServerResponse.putHeader(key, value.toString());
                        });

                        log.info("response,header:{},content:{}", responseHeader, response);
                        //设置响应内容
                        httpServerResponse
                                .end(response);
                    }
                });

        backendServer.requestHandler(backendRouter).listen(config.getPort());
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
