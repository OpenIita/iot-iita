package cc.iotkit.converter;

import org.apache.commons.lang3.StringUtils;

public class DefaultScriptConvertFactory implements IScriptConvertFactory{

    @Override
    public IConverter getCovert(String name) {
        if(StringUtils.isNotBlank(name)){
            if (name.endsWith("graaljs")){
                return new GraalJsScriptConverter();
            }
        }

        // 默认是NashornScript js实现方式
        return new ScriptConverter();
    }


}
