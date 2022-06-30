package cc.iotkit.comps;


import cc.iotkit.model.protocol.ProtocolComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ComponentManager {

    @Autowired
    private BizComponentManager bizComponentManager;

    @Autowired
    private DeviceComponentManager deviceComponentManager;


    public void register(ProtocolComponent component) {
        String type = component.getType();
        if (ProtocolComponent.TYPE_BIZ.equals(type)) {
            bizComponentManager.register(component);
        } else if (ProtocolComponent.TYPE_DEVICE.equals(type)) {
            deviceComponentManager.register(component);
        }
    }

    public void deRegister(String id) {
        bizComponentManager.deRegister(id);
        deviceComponentManager.deRegister(id);
    }

    public void start(String id) {
        bizComponentManager.start(id);
        deviceComponentManager.start(id);
    }

    public void stop(String id) {
        bizComponentManager.stop(id);
        deviceComponentManager.stop(id);
    }

    public boolean isRunning(String id) {
        return bizComponentManager.isRunning(id) || deviceComponentManager.isRunning(id);
    }

}
