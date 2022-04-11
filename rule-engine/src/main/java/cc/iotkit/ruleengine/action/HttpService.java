package cc.iotkit.ruleengine.action;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.model.device.message.ThingModelMessage;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.beanutils.BeanUtils;

import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Data
public class HttpService {
    private final NashornScriptEngine engine = (NashornScriptEngine) (new ScriptEngineManager())
            .getEngineByName("nashorn");

    private String script;

    private String url;

    private ScriptObjectMirror scriptObject;

    private OkHttpClient httpClient = new OkHttpClient();

    @SneakyThrows
    public void execute(ThingModelMessage msg) {
        if (scriptObject == null) {
            scriptObject = (ScriptObjectMirror) engine.eval("new (function(){" + script + "})()");
        }
        //执行转换脚本
        ScriptObjectMirror result = (ScriptObjectMirror) engine.invokeMethod(scriptObject, "translate", msg);
        Object objResult = JsonUtil.toObject(result);
        if (!(objResult instanceof Map)) {
            return;
        }

        HttpData httpData = new HttpData();
        BeanUtils.populate(httpData, (Map) objResult);

        //组装http请求
        String url = this.url + httpData.getPath();
        Request.Builder builder = new Request.Builder();
        Map<String, Object> headers = httpData.getHeader();
        if (headers != null) {
            headers.forEach((key, val) -> builder.header(key, val.toString()));
        }
        HttpHeader httpHeader = new HttpHeader();
        BeanUtils.populate(httpHeader, headers);

        builder.url(url);
        RequestBody requestBody;
        requestBody = RequestBody.create(MediaType.get(httpHeader.getContentType()),
                httpData.getBody().toString());

        Request request = builder.method(httpData.getMethod().toUpperCase(), requestBody).build();
        log.info("send http request:{} ,{}", url, JsonUtil.toJsonString(objResult));

        //发送请求
        try (Response response = httpClient.newCall(request).execute()) {
            ResponseBody body = response.body();
            String content = body == null ? "" : body.string();
            log.info("send result,code:{},response:{}", response.code(), content);
        } catch (IOException e) {
            throw new RuntimeException("send request failed", e);
        }

    }

    @Data
    public static class HttpData {
        private String path;
        private String method;
        private Map<String, Object> header;
        private Object body;
    }

    @Data
    public static class HttpHeader {
        private String contentType;
    }

}
