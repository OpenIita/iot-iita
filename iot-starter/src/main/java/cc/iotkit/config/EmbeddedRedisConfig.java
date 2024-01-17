/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.config;

import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;

@Slf4j
public class EmbeddedRedisConfig {

    public static boolean embeddedEnable() {
        return !"true".equals(System.getProperty("disabledEmbeddedRedis"));
    }

    public static void startEmbeddedRedisServer() {
        RedisServer redisServer;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) {
            redisServer = RedisServer.builder().setting("maxheap 200m")
                    .port(6378)
                    .setting("bind localhost")
                    .build();
        } else {
            redisServer = new RedisServer();
        }
        try {
            redisServer.start();
        } catch (Exception e) {
            if(e.getMessage().contains("Address already in use")){
                throw new RuntimeException("redis端口被占用，请先停止本地的redis服务");
            }
            log.error("start redis server failed", e);
        }
    }

}
