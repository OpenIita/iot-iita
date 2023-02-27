package cc.iotkit.engine;


import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JsNashornScriptEngine implements IScriptEngine{

    private final NashornScriptEngine engine = (NashornScriptEngine) (new ScriptEngineManager()).getEngineByName("nashorn");
    private  Object scriptObj;


    @Override
    public void setScript(String script) throws IScriptException {
        try {
            scriptObj = engine.eval(String.format("new (function () {\n%s})()", script));
        } catch (ScriptException e) {
            throw new IScriptException(e.getMessage());
        }
    }

    @Override
    public void putScriptEnv(String key, Object val) {
        engine.put(key, val);
    }

    @Override
    public Object invokeMethod(String methodName, Object... args) throws IScriptException{
        if (((ScriptObjectMirror) scriptObj).get(methodName) != null) {
            try {
                return engine.invokeMethod(scriptObj, methodName, args);
            } catch (Throwable e) {
                throw new IScriptException(e.getMessage());
            }
        }
        return null;
    }
}
