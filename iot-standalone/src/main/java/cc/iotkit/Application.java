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

import cc.iotkit.manager.config.EmbeddedElasticSearchConfig;
import cc.iotkit.manager.config.EmbeddedRedisConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Slf4j
@SpringBootApplication(scanBasePackages = {"cc.iotkit"})
@EnableWebMvc
public class Application {

    public static void main(String[] args) {
        if (EmbeddedElasticSearchConfig.embeddedEnable()) {
            EmbeddedElasticSearchConfig.startEmbeddedElasticSearch();
        }
        if (EmbeddedRedisConfig.embeddedEnable()) {
            EmbeddedRedisConfig.startEmbeddedRedisServer();
        }
        System.setProperty("nashorn.args","--no-deprecation-warning");

        SpringApplication.run(Application.class, args);
    }

}
