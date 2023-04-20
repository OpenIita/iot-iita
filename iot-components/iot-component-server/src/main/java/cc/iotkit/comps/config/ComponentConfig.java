package cc.iotkit.comps.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@Data
public class ComponentConfig {

    @Value("${component.dir:./data/components}")
    private String componentDir;

    @Value("${converter.dir:./data/converters}")
    private String converterDir;

    public Path getComponentFilePath(String comId) {
        return Paths.get(componentDir, comId)
                .toAbsolutePath().normalize();
    }

    public Path getConverterFilePath(String conId) {
        return Paths.get(converterDir, conId)
                .toAbsolutePath().normalize();
    }
}
