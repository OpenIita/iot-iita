package cc.iotkit.manager.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun")
@Data
public class AliyunConfig {

    private String bucketId;

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;
}
