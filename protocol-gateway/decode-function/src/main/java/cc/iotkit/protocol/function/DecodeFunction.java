package cc.iotkit.protocol.function;

import cc.iotkit.common.utils.JsonUtil;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import javax.script.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 上行消息转换函数
 */
public class DecodeFunction implements Function<DeviceMessage, ThingModelMessage> {

    private static final NashornScriptEngine engine = (NashornScriptEngine) (new ScriptEngineManager()).getEngineByName("nashorn");
    private static final Map<String, CompiledScript> compiledScripts = new ConcurrentHashMap<>();

    @Override
    public ThingModelMessage process(DeviceMessage msg, Context context) throws Exception {
        Optional<Object> optGateway = context.getUserConfigValue("gateway");
        Optional<Object> optScript = context.getUserConfigValue("script");
        if (!optGateway.isPresent() || !optScript.isPresent()) {
            return null;
        }

        String gateway = optGateway.get().toString();
        compiledScripts.putIfAbsent(gateway, engine.compile(optScript.get() + ".decode(msg)"));

        CompiledScript script = compiledScripts.get(gateway);
        context.getLogger().debug(script.toString());

        Map<String, Object> data = new HashMap<>();
        data.putIfAbsent("msg", msg);
        Bindings bindings = new SimpleBindings(data);
        Object result = script.eval(bindings);

        if (result == null) {
            context.getLogger().error("translate msg failed:{}", JsonUtil.toJsonString(msg));
            return null;
        }
        if (result instanceof Map) {
            return ThingModelMessage.from((Map<?, ?>) result);
        }
        return null;
    }

}
