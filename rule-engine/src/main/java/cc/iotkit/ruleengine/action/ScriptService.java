package cc.iotkit.ruleengine.action;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.model.device.message.ThingModelMessage;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.script.ScriptEngineManager;
import java.util.Map;

@Slf4j
@Data
public class ScriptService {

    private final NashornScriptEngine engine = (NashornScriptEngine) (new ScriptEngineManager())
            .getEngineByName("nashorn");

    private String script;

    private ScriptObjectMirror scriptObject;

    public Map execScript(ThingModelMessage msg) {
        try {
            if (scriptObject == null) {
                scriptObject = (ScriptObjectMirror) engine.eval("new (function(){" + script + "})()");
            }
            //执行转换脚本
            ScriptObjectMirror result = (ScriptObjectMirror) engine.invokeMethod(scriptObject, "translate", msg);
            Object objResult = JsonUtil.toObject(result);
            if (!(objResult instanceof Map)) {
                return null;
            }
            return (Map) objResult;
        } catch (Throwable e) {
            log.error("run script error", e);
            return null;
        }
    }
}
