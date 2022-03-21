package cc.iotkit.comp;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comp.model.RegisterInfo;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.script.ScriptEngineManager;
import java.util.Map;

@Slf4j
@Data
public class MessageHandler {
    private final NashornScriptEngine engine = (NashornScriptEngine) (new ScriptEngineManager()).getEngineByName("nashorn");

    private final String script;

    @SneakyThrows
    public MessageHandler(String script) {
        this.script = script;
        engine.eval(script);
    }

    public void register(Map<String, Object> head, String msg) {
    }

    public void auth(Map<String, Object> head, String msg) {
    }

    public void state(Map<String, Object> head, String msg) {
    }

    public void onReceive(Map<String, Object> head, String type, String msg) {
        try {
            ScriptObjectMirror obj = (ScriptObjectMirror) engine.invokeFunction("onReceive", head, type, msg);
            Object rstType = obj.get("type");
            if (rstType == null) {
                return;
            }
            Object data = obj.get("data");

            if ("register".equals(rstType)) {
                RegisterInfo regInfo = getData(data, RegisterInfo.class);
            } else if ("report".equals(rstType)) {

            }

        } catch (Throwable e) {
            log.error("onReceive error", e);
        }

    }

    private <T> T getData(Object data, Class<T> cls) {
        return JsonUtil.parse(JsonUtil.toJsonString(data), cls);
    }

}
