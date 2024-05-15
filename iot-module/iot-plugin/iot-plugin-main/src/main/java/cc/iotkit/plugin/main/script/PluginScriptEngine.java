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

import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.script.IScriptEngine;
import cc.iotkit.script.JavaScriptEngine;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于插件的脚本引擎，为了规避graalvm js加载问题，不直接调用使用tcp通讯交互
 *
 * @author sjg
 */
@Slf4j
@Data
public class PluginScriptEngine implements IScriptEngine {

    private String pluginId;

    private ScriptClientVerticle scriptClientVerticle = new ScriptClientVerticle();

    public PluginScriptEngine(String pluginId) {
        this.pluginId = pluginId;
        Vertx vertx = Vertx.vertx();
        Future<String> future = vertx.deployVerticle(scriptClientVerticle);
        future.onSuccess((s -> {
            log.info("tcp client started success");
        }));
        future.onFailure((e) -> {
            log.error("tcp client startup failed", e);
        });
    }

    @Override
    public void setScript(String s) {

    }

    @Override
    public void putScriptEnv(String s, Object o) {

    }

    @Override
    public void invokeMethod(String s, Object... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T invokeMethod(TypeReference<T> type, String method, Object... args) {
        List<String> argJson = new ArrayList<>();
        for (Object arg : args) {
            argJson.add(JsonUtils.toJsonString(arg));
        }

        String json = scriptClientVerticle.send(DataPackage.builder()
                .pluginId(pluginId)
                .mid(IdUtil.getSnowflakeNextIdStr())
                .method(method)
                .args(JsonUtils.toJsonString(argJson))
                .build());
        return json == null ? null : JsonUtils.parseObject(json, type);
    }

    @Override
    public String invokeMethod(String s, String s1) {
        throw new UnsupportedOperationException();
    }

}
