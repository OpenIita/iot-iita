package cc.iotkit.manager.config;

import redis.embedded.RedisServer;

public class EmbeddedRedisConfig {

    public static boolean embeddedEnable() {
        return "true".equals(System.getProperty("embeddedRedisServer"));
    }

    public static void startEmbeddedRedisServer() {
        RedisServer redisServer = RedisServer.builder().setting("maxheap 200m").build();
        redisServer.start();
    }

}
