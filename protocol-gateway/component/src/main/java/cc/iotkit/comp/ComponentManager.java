package cc.iotkit.comp;


import java.util.HashMap;
import java.util.Map;

public class ComponentManager {

    private final Map<String, Component> components = new HashMap<>();

    public void register(String id, Component component) {
        components.put(id, component);
    }

    public void deRegister(String id) {
        Component component = components.remove(id);
        component.destroy();
    }

    public void start(String id, String script) {
        Component component = components.get(id);
        if (component == null) {
            return;
        }
        component.setHandler(new MessageHandler(script, component.getConverter()));
        component.start();
    }

    public void stop(String id) {
        Component component = components.get(id);
        if (component == null) {
            return;
        }
        component.stop();
    }

}
