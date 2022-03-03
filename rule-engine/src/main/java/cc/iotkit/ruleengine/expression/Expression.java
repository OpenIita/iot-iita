package cc.iotkit.ruleengine.expression;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.script.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class Expression {

    private static final NashornScriptEngine engine = (NashornScriptEngine) (new ScriptEngineManager()).getEngineByName("nashorn");

    private static final Map<String, CompiledScript> compiledScriptMap = new ConcurrentHashMap<>();

    private static final Map<String, Comparator> comparatorMap = new HashMap<>();

    static {
        addComparator(new GtComparator());
        addComparator(new LtComparator());
        addComparator(new EqComparator());
        addComparator(new NeqComparator());
    }

    private static void addComparator(Comparator comparator) {
        comparatorMap.put(comparator.getName(), comparator);
    }

    @SneakyThrows
    private static CompiledScript create(String script) {
        return engine.compile(script);
    }

    private static boolean eval(String script, Map<String, Object> data) {
        compiledScriptMap.putIfAbsent(script, create(script));
        CompiledScript compiledScript = compiledScriptMap.get(script);
        try {
            Bindings bindings = new SimpleBindings(data);
            Object result = compiledScript.eval(bindings);
            if (result instanceof Boolean) {
                return (boolean) result;
            }
            return false;
        } catch (ScriptException e) {
            log.error("eval expression error", e);
        }
        return false;
    }

    public static boolean eval(String comparator, Object left, Object right) {
        Comparator comp = comparatorMap.get(comparator);
        if (comp == null) {
            return false;
        }
        return eval(comp.getScript(), comp.getData(left, right));
    }

}
