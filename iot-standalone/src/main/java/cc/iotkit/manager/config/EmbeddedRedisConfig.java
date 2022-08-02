/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
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
