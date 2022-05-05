package cc.iotkit.comps;


import cc.iotkit.common.ComponentClassLoader;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.comp.CompConfig;
import cc.iotkit.comp.IComponent;
import cc.iotkit.comps.config.ComponentConfig;
import cc.iotkit.comps.service.DeviceBehaviourService;
import cc.iotkit.dao.ProtocolComponentRepository;
import cc.iotkit.model.protocol.ProtocolComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class BizComponentManager {

    private final Map<String, IComponent> components = new HashMap<>();
    private final Map<String, Boolean> states = new HashMap<>();

    @Autowired
    private ComponentConfig componentConfig;
    @Autowired
    private ProtocolComponentRepository componentRepository;
    @Autowired
    private DeviceBehaviourService deviceBehaviourService;

    @PostConstruct
    public void init() {
        try {
            List<ProtocolComponent> componentList = componentRepository
                    .findByStateAndType(ProtocolComponent.STATE_RUNNING, ProtocolComponent.TYPE_BIZ);
            for (ProtocolComponent component : componentList) {
                register(component);
                start(component.getId());
            }
        } catch (Throwable e) {
            log.error("init protocol components error", e);
        }
    }

    public void register(ProtocolComponent component) {
        String id = component.getId();
        if (components.containsKey(id)) {
            return;
        }

        Path path = componentConfig.getComponentFilePath(id);
        File file = path.resolve(component.getJarFile()).toAbsolutePath().toFile();
        IComponent componentInstance;
        try {
            componentInstance = ComponentClassLoader.getComponent(component.getId(), file);
        } catch (Throwable e) {
            throw new BizException("get component instance error");
        }
        try {
            String componentScript = FileUtils.readFileToString(path.
                    resolve(ProtocolComponent.SCRIPT_FILE_NAME).toFile(), "UTF-8");
            componentInstance.setScript(componentScript);
            componentInstance.putScriptEnv("deviceBehaviour", deviceBehaviourService);
        } catch (IOException e) {
            throw new BizException("get component script error", e);
        }
        componentInstance.create(new CompConfig(300, component.getConfig()));

        register(id, componentInstance);
    }

    public void register(String id, IComponent component) {
        components.put(id, component);
        states.put(id, false);
    }

    public void deRegister(String id) {
        IComponent component = components.remove(id);
        states.remove(id);
        if (component == null) {
            return;
        }
        component.stop();
        component.destroy();
    }

    public void start(String id) {
        IComponent component = components.get(id);
        if (component == null) {
            return;
        }
        component.start();
        states.put(id, true);
    }

    public void stop(String id) {
        IComponent component = components.get(id);
        if (component == null) {
            return;
        }
        component.stop();
        states.put(id, false);
    }

    public boolean isRunning(String id) {
        return states.containsKey(id) && states.get(id);
    }

}
