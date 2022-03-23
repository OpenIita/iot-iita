package cc.iotkit.converter;

import cc.iotkit.model.device.message.ThingModelMessage;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

@Slf4j
@Data
public class ScriptConverter implements IConverter {
    private final NashornScriptEngine engine = (NashornScriptEngine) (new ScriptEngineManager()).getEngineByName("nashorn");

    private String script;

    public void setScript(String script) {
        this.script = script;
        try {
            engine.eval(script);
        } catch (ScriptException e) {
            log.error("eval converter script error", e);
        }
    }

    public ThingModelMessage decode(String msg) {
        try {
            ScriptObjectMirror result = (ScriptObjectMirror) engine.invokeFunction("decode", msg);
            ThingModelMessage modelMessage = new ThingModelMessage();
            BeanUtils.populate(modelMessage, result);
            return modelMessage;
        } catch (Throwable e) {
            log.error("execute decode script error", e);
        }
        return null;
    }

    @Override
    public String encode(DeviceService<?> service, Device device) {
        try {
            ScriptObjectMirror result = (ScriptObjectMirror) engine.invokeFunction("encode", service, device);
            return result.toString();
        } catch (Throwable e) {
            log.error("execute encode script error", e);
        }
        return null;
    }

}
