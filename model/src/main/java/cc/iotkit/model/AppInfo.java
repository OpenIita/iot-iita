package cc.iotkit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppInfo {

    private String appName;

    private String packageName;

    private String version;

    private String buildNumber;

    /**
     * 安装包下载路径
     */
    private String packageUrl;
}
