package cc.iotkit.script;

import cc.iotkit.common.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;

import java.util.Objects;

public class JavaScriptEngine implements IScriptEngine {

    private final Context context = Context.newBuilder("js").allowHostAccess(HostAccess.ALL).build();

    private Value jsScript;

    @Override
    public void setScript(String script) {
        jsScript = context.eval("js", String.format(
                "new (function () {\n%s; " +
                        "   this.invoke=function(f,args){" +
                        "       for(i in args){" +
                        "           args[i]=JSON.parse(args[i]);" +
                        "       }" +
                        "       return JSON.stringify(this[f].apply(this,args));" +
                        "   }; " +
                        "})()", script));
    }

    @Override
    public void putScriptEnv(String key, Object value) {
        context.getBindings("js").putMember(key, value);
    }

    @Override
    public void invokeMethod(String methodName, Object... args) {
        invokeMethod(new TypeReference<Void>() {
        }, methodName, args);
    }

    @Override
    public <T> T invokeMethod(TypeReference<T> type, String methodName, Object... args) {
        Value member = jsScript.getMember("invoke");

        if (Objects.isNull(member)) {
            return null;
        }

        for (int i = 0; i < args.length; i++) {
            args[i] = JsonUtil.toJsonString(args[i]);
        }

        //通过调用invoke方法将目标方法返回结果转成json
        Value rst = member.execute(methodName, args);
        if (rst == null) {
            return null;
        }

        String json = rst.asString();
        if (json == null) {
            return null;
        }

        return JsonUtil.parse(json, type);
    }

}