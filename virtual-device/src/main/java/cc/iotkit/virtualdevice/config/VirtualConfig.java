package cc.iotkit.virtualdevice.config;

import cc.iotkit.virtualdevice.VirtualManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VirtualConfig {

    @Bean
    public VirtualManager getVirtualManager() {
        return new VirtualManager();
    }

}
