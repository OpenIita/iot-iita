package cc.iotkit.server.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

//@Configuration
//@EnableCaching
public class CacheConfig {

    /**
     * 配置本地缓存
     */
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Lists.newArrayList(new CaffeineCache(
                        "device_cache",
                        Caffeine.newBuilder()
                                .expireAfterWrite(5, TimeUnit.MINUTES)
                                .build()
                ),
                new CaffeineCache(
                        "product_cache",
                        Caffeine.newBuilder()
                                .expireAfterWrite(5, TimeUnit.MINUTES)
                                .build()
                ),
                new CaffeineCache(
                        "app_design_cache",
                        Caffeine.newBuilder()
                                .expireAfterWrite(5, TimeUnit.MINUTES)
                                .build()
                )));
        return manager;
    }

}
