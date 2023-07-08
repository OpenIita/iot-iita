/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit;

import cc.iotkit.config.EmbeddedElasticSearchConfig;
import cc.iotkit.config.EmbeddedRedisConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Slf4j
@SpringBootApplication(scanBasePackages = {"cc.iotkit"})
@EnableTransactionManagement
@EnableWebMvc
@EnableFeignClients(basePackages = {"cc.iotkit.baetyl.feign"})
public class Application {

    public static void main(String[] args) {
        //System.setProperty("disabledEmbeddedEs","true");
        //System.setProperty("disabledEmbeddedRedis","true");
        if (EmbeddedElasticSearchConfig.embeddedEnable()) {
            EmbeddedElasticSearchConfig.startEmbeddedElasticSearch();
        }
        if (EmbeddedRedisConfig.embeddedEnable()) {
            EmbeddedRedisConfig.startEmbeddedRedisServer();
        }

        SpringApplication.run(Application.class, args);
        log.info("server start success!");
    }

}
