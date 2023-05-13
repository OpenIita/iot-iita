package cc.iotkit.screen.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author：tfd
 * @Date：2023/5/6 16:11
 */
@Configuration
@Data
public class BigScreenConfig {
    @Value("${bigScreen.dir:./data/screens}")
    private String bigScreenDir;

    public Path getBigScreenFilePath(String bigScreenId) {
        return Paths.get(bigScreenDir, bigScreenId)
                .toAbsolutePath().normalize();
    }
}
