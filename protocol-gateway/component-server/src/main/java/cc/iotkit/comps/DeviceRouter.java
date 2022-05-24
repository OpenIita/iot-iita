package cc.iotkit.comps;

import cc.iotkit.comp.IComponent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备路由
 */
@Component
public class DeviceRouter {

    private static final String DEVICE_ROUTER = "str:device:router:%s:%s";

    private static final Map<String, IComponent> components = new HashMap<>();

    @Autowired
    private StringRedisTemplate redisTemplate;

    private String getDeviceRouter(String productKey, String deviceName) {
        return String.format(DEVICE_ROUTER, productKey, deviceName);
    }

    public void putRouter(String productKey, String deviceName, IComponent component) {
        String comId = component.getId();
        components.put(comId, component);
        redisTemplate.opsForValue().set(getDeviceRouter(productKey, deviceName), component.getId());
    }

    public void removeRouter(String productKey, String deviceName) {
        redisTemplate.delete(getDeviceRouter(productKey, deviceName));
    }

    public IComponent getRouter(String productKey, String deviceName) {
        String comId = redisTemplate.opsForValue().get(getDeviceRouter(productKey, deviceName));
        if (StringUtils.isBlank(comId)) {
            return null;
        }
        return components.get(comId);
    }

}
