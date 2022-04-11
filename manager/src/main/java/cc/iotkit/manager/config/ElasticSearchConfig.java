package cc.iotkit.manager.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.InternalSettingsPreparer;
import org.elasticsearch.node.Node;
import org.elasticsearch.transport.Netty4Plugin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Configuration
public class ElasticSearchConfig {

    static {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    @SneakyThrows
    @Bean
    public EmbeddedElasticSearch getEmbeddedElasticSearch(ConfigProperty configProperty) {
        if (configProperty.enabled) {
            EmbeddedElasticSearch embeddedElasticSearch = new EmbeddedElasticSearch(configProperty);
            embeddedElasticSearch.start();
            return embeddedElasticSearch;
        }
        return null;
    }

    @Component
    @ConfigurationProperties(prefix = "elasticsearch.embedded")
    public static class ConfigProperty {

        private boolean enabled;

        private String dataPath = "./data/elasticsearch";

        private String homePath = "./";

        private int port = 9200;

        private String host = "0.0.0.0";

        public Settings.Builder applySetting(Settings.Builder settings) {
            return settings.put("network.host", host)
                    .put("http.port", port)
                    .put("path.data", dataPath)
                    .put("path.home", homePath);
        }

    }

    public static class EmbeddedElasticSearch extends Node {

        @SneakyThrows
        public EmbeddedElasticSearch(ConfigProperty properties) {
            super(InternalSettingsPreparer.prepareEnvironment(
                    properties.applySetting(
                            Settings.builder()
                                    .put("node.name", "test")
                                    .put("discovery.type", "single-node")
                                    .put("transport.type", Netty4Plugin.NETTY_TRANSPORT_NAME)
                                    .put("http.type", Netty4Plugin.NETTY_HTTP_TRANSPORT_NAME)
                                    .put("network.host", "0.0.0.0")
                                    .put("http.port", 9200)
                    ).build(), Collections.emptyMap(), null, () -> "default"),
                    Collections.singleton(Netty4Plugin.class), false);
        }
    }
}
