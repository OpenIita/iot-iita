package cc.iotkit.data.cache;

import cc.iotkit.common.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DeviceCacheService {

    private static final String PROPERTY_CACHE_KEY = "str:device:property:%s";

    @Autowired
    private StringRedisTemplate redisTemplate;

    private String getPropertyCacheKey(String deviceId) {
        return String.format(PROPERTY_CACHE_KEY, deviceId);
    }

    /**
     * 保存设备属性到redis
     *
     * @param deviceId   设备id
     * @param properties 设备属性map
     */
    public void saveProperties(String deviceId, Map<String, Object> properties) {
        redisTemplate.opsForValue().set(getPropertyCacheKey(deviceId), JsonUtil.toJsonString(properties));
    }

    /**
     * 获取设备属性map
     *
     * @param deviceId 设备id
     */
    public Map<String, Object> getProperties(String deviceId) {
        return JsonUtil.parse(redisTemplate.opsForValue().get(getPropertyCacheKey(deviceId)), Map.class);
    }

}
