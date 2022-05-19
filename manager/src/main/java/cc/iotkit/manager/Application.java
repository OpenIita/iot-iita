package cc.iotkit.manager;

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

        SpringApplication.run(Application.class, args);
    }

}
