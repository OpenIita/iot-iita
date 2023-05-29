/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.config;

import lombok.SneakyThrows;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.InternalSettingsPreparer;
import org.elasticsearch.node.Node;
import org.elasticsearch.transport.Netty4Plugin;

import java.util.Collections;

public class EmbeddedElasticSearchConfig {

    static {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    public static boolean embeddedEnable() {
        return !"true".equals(System.getProperty("disabledEmbeddedEs"));
    }

    @SneakyThrows
    public static void startEmbeddedElasticSearch() {
        EmbeddedElasticSearch embeddedElasticSearch = new EmbeddedElasticSearch(new ConfigProperty());
        embeddedElasticSearch.start();
    }

    public static class ConfigProperty {

        public Settings.Builder applySetting(Settings.Builder settings) {
            String dataPath = "./data/elasticsearch";
            String homePath = "./";
            int port = 9200;
            String host = "0.0.0.0";
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
