package cc.iotkit.baetyl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "baetyl")
public class BaetylProperties {

    /**
     * 自定义baetyl服务地址
     */
    private String serviceUrl;
    /**
     * api请求封装类型 feign webclient
     */
    private String apiType;
}
