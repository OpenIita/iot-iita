package cc.iotkit.comps;


import cc.iotkit.comp.IComponent;
import cc.iotkit.comps.service.DeviceBehaviourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ComponentManager {

    private final Map<String, IComponent> components = new HashMap<>();

    @Autowired
    private DeviceBehaviourService deviceBehaviourService;

    public void register(String id, IComponent component) {
        components.put(id, component);
    }

    public void deRegister(String id) {
        IComponent component = components.remove(id);
        component.destroy();
    }

    public void start(String id, String script) {
        IComponent component = components.get(id);
        if (component == null) {
            return;
        }
        component.setHandler(new MessageHandler(script, component.getConverter(),
                deviceBehaviourService));
        component.start();
    }

    public void stop(String id) {
        IComponent component = components.get(id);
        if (component == null) {
            return;
        }
        component.stop();
    }

}
