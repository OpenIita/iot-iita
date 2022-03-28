package cc.iotkit.comps;


import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.comp.IComponent;
import cc.iotkit.comps.config.CacheKey;
import cc.iotkit.comps.service.DeviceBehaviourService;
import cc.iotkit.converter.DeviceMessage;
import cc.iotkit.converter.ThingService;
import cc.iotkit.model.device.message.ThingModelMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ComponentManager {

    private final Map<String, IComponent> components = new HashMap<>();

    @Autowired
    private DeviceBehaviourService deviceBehaviourService;
    @Autowired
    private StringRedisTemplate redisTemplate;

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
        component.setHandler(
                new MessageHandler(this, component,
                        script, component.getConverter(),
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
