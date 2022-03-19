package cc.iotkit.protocol.function;

import cc.iotkit.common.utils.JsonUtil;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息分发函数
 */
public class MessageDistributionFunction implements Function<ThingModelMessage, ThingModelMessage> {

    private static final NashornScriptEngine engine = (NashornScriptEngine) (new ScriptEngineManager()).getEngineByName("nashorn");
    private static final Map<String, CompiledScript> compiledScripts = new ConcurrentHashMap<>();

    @Override
    public ThingModelMessage process(ThingModelMessage msg, Context context) throws Exception {
        Optional<Object> optName = context.getUserConfigValue("name");
        Optional<Object> optScript = context.getUserConfigValue("script");
        if (!optName.isPresent() || !optScript.isPresent()) {
            return null;
        }

        String name = optName.get().toString();
        compiledScripts.putIfAbsent(name, engine.compile(optScript.get().toString()));

        CompiledScript script = compiledScripts.get(name);
        context.getLogger().debug(script.toString());

        Map<String, Object> data = new HashMap<>();
        data.putIfAbsent("msg", msg);
        Bindings bindings = new SimpleBindings(data);
        Object result = script.eval(bindings);

        if (result == null) {
            context.getLogger().error("translate msg failed:{}", JsonUtil.toJsonString(msg));
            return null;
        }
        return JsonUtil.parse(JsonUtil.toJsonString(result), ThingModelMessage.class);
    }


}
