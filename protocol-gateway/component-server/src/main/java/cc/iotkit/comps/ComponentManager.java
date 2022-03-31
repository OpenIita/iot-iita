package cc.iotkit.comps;


import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comp.CompConfig;
import cc.iotkit.comp.IComponent;
import cc.iotkit.comps.config.CacheKey;
import cc.iotkit.comps.config.ComponentConfig;
import cc.iotkit.comps.service.DeviceBehaviourService;
import cc.iotkit.converter.DeviceMessage;
import cc.iotkit.converter.ScriptConverter;
import cc.iotkit.converter.ThingService;
import cc.iotkit.dao.ProtocolComponentRepository;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.model.protocol.ProtocolComponent;
import cc.iotkit.model.protocol.ProtocolConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ComponentManager {

    private final Map<String, IComponent> components = new HashMap<>();
    private final Map<String, String> scripts = new HashMap<>();
    private final Map<String, Boolean> states = new HashMap<>();

    @Autowired
    private DeviceBehaviourService deviceBehaviourService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ComponentConfig componentConfig;
    @Autowired
    private ProtocolComponentRepository componentRepository;

    @PostConstruct
    public void init() {
        List<ProtocolComponent> componentList = componentRepository.findByState(ProtocolComponent.STATE_RUNNING);
        for (ProtocolComponent component : componentList) {
            register(component);
            start(component.getId());
        }
    }

    public void register(ProtocolComponent component) {
        String id = component.getId();
        if (components.containsKey(id)) {
            return;
        }

        Path path = componentConfig.getComponentFilePath(id);
        File file = path.resolve(component.getJarFile()).toAbsolutePath().toFile();
        IComponent componentInstance = ComponentClassLoader.getComponent(file);
        if (componentInstance == null) {
            throw new BizException("instance component failed");
        }
        componentInstance.create(new CompConfig(300, component.getConfig()));

        try {
            ScriptConverter scriptConverter = new ScriptConverter();
            Path converterPath = componentConfig.getConverterFilePath(component.getConverter());
            String converterScript = FileUtils.readFileToString(converterPath.
                    resolve(ProtocolConverter.SCRIPT_FILE_NAME).toFile(), "UTF-8");

            scriptConverter.setScript(converterScript);
            componentInstance.setConverter(scriptConverter);

            String componentScript = FileUtils.readFileToString(path.
                    resolve(ProtocolComponent.SCRIPT_FILE_NAME).toFile(), "UTF-8");
            register(id, componentInstance, componentScript);
        } catch (IOException e) {
            throw new BizException("get component script error", e);
        }
    }

    public void register(String id, IComponent component, String script) {
        components.put(id, component);
        scripts.put(id, script);
        states.put(id, false);
    }

    public void deRegister(String id) {
        IComponent component = components.remove(id);
        scripts.remove(id);
        states.remove(id);
        component.stop();
        component.destroy();
    }

    public void start(String id) {
        IComponent component = components.get(id);
        if (component == null) {
            return;
        }
        String script = scripts.get(id);
        component.setHandler(
                new MessageHandler(this, component,
                        script, component.getConverter(),
                        deviceBehaviourService));
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

    public void send(ThingService<?> service) {
        log.info("start exec device service:{}", JsonUtil.toJsonString(service));
        if (components.size() == 0) {
            throw new BizException("there is no components");
        }

        for (IComponent com : components.values()) {
            if (com.exist(service.getProductKey(), service.getDeviceName())) {
                DeviceMessage message = com.getConverter().encode(service, null);
                if (message == null) {
                    throw new BizException("encode send message failed");
                }
                //保存设备端mid与平台mid对应关系
                redisTemplate.opsForValue().set(
                        CacheKey.getKeyCmdMid(service.getDeviceName(), message.getMid()),
                        service.getMid(), com.getConfig().getCmdTimeout(), TimeUnit.SECONDS);
                com.send(message);

                ThingModelMessage thingModelMessage = ThingModelMessage.builder()
                        .mid(service.getMid())
                        .productKey(service.getProductKey())
                        .deviceName(service.getDeviceName())
                        .identifier(service.getIdentifier())
                        .type(service.getType())
                        .data(service.getParams())
                        .build();
                deviceBehaviourService.reportMessage(thingModelMessage);

                return;
            }
        }
        throw new BizException("send destination not found");
    }

    public String getPlatformMid(String deviceName, String mid) {
        return redisTemplate.opsForValue().get(CacheKey.getKeyCmdMid(deviceName, mid));
    }

}
