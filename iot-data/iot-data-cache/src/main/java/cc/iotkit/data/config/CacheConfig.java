package cc.iotkit.data.config;

import cc.iotkit.common.Constants;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheConfiguration getRedisCacheConfiguration() {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        cacheConfiguration = cacheConfiguration.serializeValuesWith(RedisSerializationContext
                .SerializationPair.fromSerializer(RedisSerializer.json()));
        return cacheConfiguration;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory, RedisCacheConfiguration config) {
        Set<String> cacheNames = Set.of(Constants.CACHE_DEVICE_INFO, Constants.CACHE_DEVICE_STATS);
        Map<String, RedisCacheConfiguration> cacheConfigs = Map.of(
                Constants.CACHE_DEVICE_INFO, config,
                Constants.CACHE_PRODUCT, config,
                Constants.CACHE_OAUTH_CLIENT, config,
                Constants.CACHE_DEVICE_STATS, config.entryTtl(Duration.ofMinutes(5))
        );

        return RedisCacheManager.builder(factory)
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }

}
