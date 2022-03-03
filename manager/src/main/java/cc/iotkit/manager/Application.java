package cc.iotkit.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {"cc.iotkit.deviceapi"})
@SpringBootApplication(scanBasePackages = {"cc.iotkit.manager", "cc.iotkit.dao", "cc.iotkit.ruleengine"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
