package cc.iotkit.manager.config;

import redis.embedded.RedisServer;

public class EmbeddedRedisConfig {

    public static boolean embeddedEnable() {
        return !"true".equals(System.getProperty("disabledEmbeddedRedis"));
    }

    public static void startEmbeddedRedisServer() {
        RedisServer redisServer;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) {
            redisServer = RedisServer.builder().setting("maxheap 200m").build();
        } else {
            redisServer = new RedisServer();
        }
        redisServer.start();
    }

}
