package cc.iotkit.fun;

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

public class TestFunction implements Function<byte[], byte[]> {

    private static final NashornScriptEngine engine = (NashornScriptEngine) (new ScriptEngineManager()).getEngineByName("nashorn");
    private static final Map<String, CompiledScript> compiledScripts = new ConcurrentHashMap<>();

    @Override
    public byte[] process(byte[] bs, Context context) throws Exception {
        Optional<Object> objPk = context.getUserConfigValue("pk");
        Optional<Object> objTrans = context.getUserConfigValue("transform");
        if (!objPk.isPresent() || !objTrans.isPresent()) {
            return null;
        }
        String s = new String(bs);
        String pk = objPk.get().toString();
        compiledScripts.putIfAbsent(pk, engine.compile(objTrans.get().toString()));

        CompiledScript script = compiledScripts.get(pk);
        context.getLogger().debug(script.toString());

        Map<String, Object> data = new HashMap<>();
        data.putIfAbsent("msg", s);
        Bindings bindings = new SimpleBindings(data);
        Object result = script.eval(bindings);

        if (result == null) {
            context.getLogger().error("translate failed:" + s);
            return null;
        }
        s = JsonUtil.toJsonString(result);
        return s.getBytes();
    }


}
