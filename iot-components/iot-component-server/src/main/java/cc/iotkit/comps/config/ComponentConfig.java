package cc.iotkit.comps.config;

import cc.iotkit.converter.IScriptConvertFactory;
import cc.iotkit.converter.DefaultScriptConvertFactory;
import cc.iotkit.engine.DefaultScriptEngineFactory;
import cc.iotkit.engine.IScriptEngineFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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

    @Bean("objectMapper")
    public ObjectMapper myMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }


    @Bean
    public IScriptConvertFactory scriptConverterFactory(){
        return new DefaultScriptConvertFactory();
    }

    @Bean
    public IScriptEngineFactory scriptEngineFactory(){
        return new DefaultScriptEngineFactory();
    }
}
