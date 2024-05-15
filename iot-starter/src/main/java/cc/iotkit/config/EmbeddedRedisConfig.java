/*
 *
 *  * | Licensed 未经许可不能去掉「OPENIITA」相关版权
 *  * +----------------------------------------------------------------------
 *  * | Author: xw2sy@163.com
 *  * +----------------------------------------------------------------------
 *
 *  Copyright [2024] [OPENIITA]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */
package cc.iotkit.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class EmbeddedRedisConfig {

    @Value("${spring.redis.embedded.enabled:false}")
    private  boolean embeddedRedisEnabled;
    @Value("${spring.redis.port:6378}")
    private  Integer redisPort;

    @PostConstruct
    private void start(){
        if (embeddedRedisEnabled) {
            startEmbeddedRedisServer(redisPort);
        }

    }
    public static void startEmbeddedRedisServer(Integer port) {
        RedisServer redisServer;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) {
            redisServer = RedisServer.builder().setting("maxheap 200m")
                    .port(port)
                    .setting("bind localhost")
                    .build();
        } else {
            redisServer = RedisServer.builder()
                    .port(port)
                    .setting("bind localhost")
                    .build();
        }
        try {
            redisServer.start();
        } catch (Exception e) {
            if (e.getMessage().contains("Address already in use")) {
                throw new RuntimeException("redis端口被占用，请先停止本地的redis服务");
            }
            log.error("start redis server failed", e);
        }
    }

}
