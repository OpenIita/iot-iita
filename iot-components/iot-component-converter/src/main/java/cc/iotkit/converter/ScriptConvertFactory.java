package cc.iotkit.converter;

public class ScriptConvertFactory implements IScriptConvertFactory{

    @Override
    public IConverter getCovert(String name) {
        if (name.endsWith("graaljs")){
            return new GraalJsScriptConverter();
        }

        // 默认是NashornScript js实现方式
        return new ScriptConverter();
    }


}
