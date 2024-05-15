/*
 *
 *  * | Licensed 未经许可不能去掉「OPENIITA」相关版权
 *  * +----------------------------------------------------------------------
 *  * | Author: xw2sy@163.com
 *  * +----------------------------------------------------------------------
 *
 *  Copyright [2024] [OPENIITA]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */
package cc.iotkit.config;

import lombok.SneakyThrows;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.InternalSettingsPreparer;
import org.elasticsearch.node.Node;
import org.elasticsearch.transport.Netty4Plugin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Component
public class EmbeddedElasticSearchConfig {

    @Value("${spring.elasticsearch.embedded.enabled:false}")
    private  boolean embeddedElasticsearchEnabled;

    @Value("${spring.elasticsearch.embedded.port:9200}")
    private  Integer esPort;

    @PostConstruct
    private void start(){
        if (embeddedElasticsearchEnabled) {
            startEmbeddedElasticSearch(esPort);
        }

    }

    @SneakyThrows
    public  void startEmbeddedElasticSearch(Integer port) {
        EmbeddedElasticSearch embeddedElasticSearch = new EmbeddedElasticSearch(new ConfigProperty(), port);
        embeddedElasticSearch.start();
    }

    public class ConfigProperty {

        public Settings.Builder applySetting(Settings.Builder settings) {
            String dataPath = "./data/elasticsearch";
            String homePath = "./";
            int port = esPort;
            String host = "0.0.0.0";
            return settings.put("network.host", host)
                    .put("http.port", port)
                    .put("path.data", dataPath)
                    .put("path.home", homePath);
        }

    }

    public static class EmbeddedElasticSearch extends Node {

        @SneakyThrows
        public EmbeddedElasticSearch(ConfigProperty properties, Integer port) {
            super(InternalSettingsPreparer.prepareEnvironment(
                            properties.applySetting(
                                    Settings.builder()
                                            .put("node.name", "test")
                                            .put("discovery.type", "single-node")
                                            .put("transport.type", Netty4Plugin.NETTY_TRANSPORT_NAME)
                                            .put("http.type", Netty4Plugin.NETTY_HTTP_TRANSPORT_NAME)
                                            .put("network.host", "0.0.0.0")
                                            .put("http.port", port)
                            ).build(), Collections.emptyMap(), null, () -> "default"),
                    Collections.singleton(Netty4Plugin.class), false);
        }
    }
}
