package cc.iotkit.protocol.server;

import cc.iotkit.common.utils.JsonUtil;
import jdk.nashorn.api.scripting.NashornScriptEngine;

import javax.script.*;
import java.util.HashMap;
import java.util.Map;

public class Test3 {
    private static final NashornScriptEngine engine = (NashornScriptEngine) (new ScriptEngineManager()).getEngineByName("nashorn");


    public static void main(String[] args) throws ScriptException {

        Map<String, Object> data = new HashMap<>();
        data.putIfAbsent("msg", "aaa");
        CompiledScript script = engine.compile("a={data:msg+'===111',b:2}");
        Bindings bindings = new SimpleBindings(data);
        Object result = script.eval(bindings);
        System.out.println(JsonUtil.toJsonString(result));

        script = engine.compile("new (function() {\n" +
                "  function add(n){\n" +
                "      return n+1;\n" +
                "  }\n" +
                "  this.decode = function(msg) {\n" +
                "    return \"=>decode:\"+add(msg);\n" +
                "  };\n" +
                "})().decode(color)");

        for (int i = 0; i < 100; i++) {
            data.put("color", "black" + i);
            bindings = new SimpleBindings(data);
            result = script.eval(bindings);
            System.out.println(result);
        }

    }
}
