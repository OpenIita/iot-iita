package cc.iotkit.engine;

import org.graalvm.polyglot.*;

import java.util.Objects;

public class JsGraalJsScriptEngine implements IScriptEngine{

    private final Context context = Context.newBuilder("js").allowHostAccess(true).build();

    private Value jsScript;

    @Override
    public void setScript(String script) {
         jsScript = context.eval("js", String.format("new (function () {\n%s})()", script));

    }

    @Override
    public void putScriptEnv(String key, Object value) {
        context.getBindings("js").putMember(key, value);
    }

    @Override
    public Object invokeMethod(String methodName, Object... args) throws IScriptException {

        Value member = jsScript.getMember(methodName);
        if(Objects.nonNull(member)){
            Value execute = member.execute(args);

            return execute.as(Object.class);
        }
        return null;
    }
}
