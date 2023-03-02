package cc.iotkit.engine;

import org.apache.commons.lang3.StringUtils;

public class DefaultScriptEngineFactory implements IScriptEngineFactory{
    @Override
    public IScriptEngine getScriptEngine(String name) {
        if(StringUtils.isNotBlank(name)){
            if (name.endsWith("graaljs")){
                return new JsGraalJsScriptEngine();
            }
        }

        // 默认是NashornScript js实现方式
        return new JsNashornScriptEngine();
    }
}
