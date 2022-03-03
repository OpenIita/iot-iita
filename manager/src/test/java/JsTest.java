import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import static javax.script.ScriptContext.GLOBAL_SCOPE;

public class JsTest {
    public static void main(String[] args) throws ScriptException, IOException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        SimpleBindings bindings = new SimpleBindings();
        engine.getContext().setBindings(bindings, GLOBAL_SCOPE);
        engine.eval("print('aaaaaa')");
        bindings.put("aa", "test");
        engine.eval("print(aa);", bindings);

        bindings.put("test", (Runnable) () -> System.out.println("test func"));
        engine.eval("test()", bindings);

        ScriptEngineManager manager2 = new ScriptEngineManager();
        ScriptEngine engine2 = manager.getEngineByName("nashorn");
        engine2.eval(new InputStreamReader(URI.create("https://requirejs.org/docs/release/2.3.6/minified/require.js").toURL().openStream()));

        ScriptEngineManager manager3 = new ScriptEngineManager();
        ScriptEngine engine3 = manager.getEngineByName("nashorn");
        engine3.eval("print(requirejs);",engine2.getContext());


    }
}
