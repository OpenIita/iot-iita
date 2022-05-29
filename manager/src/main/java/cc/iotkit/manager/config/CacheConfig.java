package cc.iotkit.manager.config;

import cc.iotkit.common.Constants;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 配置本地缓存
     */
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Lists.newArrayList(new CaffeineCache(
                        Constants.DEVICE_CACHE,
                        Caffeine.newBuilder()
                                .expireAfterWrite(5, TimeUnit.MINUTES)
                                .build()
                ), new CaffeineCache(
                        Constants.DEVICE_STATS_CACHE,
                        Caffeine.newBuilder()
                                .expireAfterWrite(5, TimeUnit.MINUTES)
                                .build()
                ),
                new CaffeineCache(
                        Constants.PRODUCT_CACHE,
                        Caffeine.newBuilder()
                                .expireAfterWrite(5, TimeUnit.MINUTES)
                                .build()
                ),
                new CaffeineCache(
                        Constants.APP_DESIGN_CACHE,
                        Caffeine.newBuilder()
                                .expireAfterWrite(5, TimeUnit.MINUTES)
                                .build()
                ),
                new CaffeineCache(
                        Constants.THING_MODEL_CACHE,
                        Caffeine.newBuilder()
                                .expireAfterWrite(5, TimeUnit.MINUTES)
                                .build()
                ),
                new CaffeineCache(
                        Constants.PRODUCT_SCRIPT_CACHE,
                        Caffeine.newBuilder()
                                .expireAfterWrite(5, TimeUnit.MINUTES)
                                .build()
                ), new CaffeineCache(
                        Constants.SPACE_CACHE,
                        Caffeine.newBuilder()
                                .expireAfterWrite(5, TimeUnit.MINUTES)
                                .build()
                ), new CaffeineCache(
                        Constants.CATEGORY_CACHE,
                        Caffeine.newBuilder()
                                .expireAfterWrite(5, TimeUnit.MINUTES)
                                .build()
                ), new CaffeineCache(
                        Constants.USER_CACHE,
                        Caffeine.newBuilder()
                                .expireAfterWrite(5, TimeUnit.MINUTES)
                                .build()
                ), new CaffeineCache(
                        Constants.OAUTH_CLIENT_CACHE,
                        Caffeine.newBuilder()
                                .expireAfterWrite(5, TimeUnit.MINUTES)
                                .build()
                )
        ));
        return manager;
    }

}
