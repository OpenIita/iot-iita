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

package cc.iotkit.plugin.main.script;

import cc.iotkit.data.manager.IPluginInfoData;
import cc.iotkit.script.IScriptEngine;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于插件的脚本引擎，为了规避graalvm js加载问题，不直接调用使用tcp通讯交互
 *
 * @author sjg
 */
@Slf4j
@Service
public class PluginScriptServer {
    @Autowired
    private IPluginInfoData pluginInfoData;

    @Autowired
    private ScriptVerticle scriptVerticle;

    private static final Map<String, IScriptEngine> PLUGIN_SCRIPT_ENGINES = new HashMap<>();

    @PostConstruct
    public void init() {
        Vertx vertx = Vertx.vertx();
        Future<String> future = vertx.deployVerticle(scriptVerticle);
        future.onSuccess((s -> {
            log.info("plugin script server started success");
        }));
        future.onFailure(Throwable::printStackTrace);
    }

    public IScriptEngine getScriptEngine(String pluginId) {
        if (!PLUGIN_SCRIPT_ENGINES.containsKey(pluginId)) {
            PLUGIN_SCRIPT_ENGINES.put(pluginId,new PluginScriptEngine(pluginId));
        }
        return PLUGIN_SCRIPT_ENGINES.get(pluginId);
    }

}
