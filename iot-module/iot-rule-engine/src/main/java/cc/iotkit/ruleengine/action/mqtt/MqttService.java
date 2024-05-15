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

package cc.iotkit.ruleengine.action.mqtt;

import cc.iotkit.common.thing.ThingModelMessage;
import cc.iotkit.common.utils.FIUtil;
import cc.iotkit.ruleengine.action.ScriptService;
import cc.iotkit.ruleengine.link.LinkFactory;
import cc.iotkit.ruleengine.link.LinkService;
import cc.iotkit.ruleengine.link.impl.MqttClientLink;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author huangwenl
 * @date 2022-11-09
 */

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class MqttService extends ScriptService implements LinkService {

    private String username;
    private String password;
    private String host;
    private int port;

    public String execute(ThingModelMessage msg) {
        //执行转换脚本
        Map<String, Object> result = execScript(new TypeReference<>() {
        }, msg);
        if (result == null) {
            log.warn("execScript result is null");
            return "execScript result is null";
        }
        boolean initResult = LinkFactory.initLink(getKey(), MqttClientLink.LINK_TYPE, getLinkConf());

        AtomicReference<String> data = new AtomicReference<>("");
        FIUtil.isTotF(initResult).handler(
                () -> LinkFactory.sendMsg(getKey(), result, data::set),
                () -> data.set("创建连接失败！")
        );
        return data.get();
    }

    @Override
    public String getKey() {
        return String.format("mqtt_%s_%d", host, port);
    }

    @Override
    public String getLinkType() {
        return MqttClientLink.LINK_TYPE;
    }

    @Override
    public Map<String, Object> getLinkConf() {
        Map<String, Object> config = new HashMap<>();
        config.put(MqttClientLink.HOST, host);
        config.put(MqttClientLink.PORT, port);
        config.put(MqttClientLink.USERNAME, username);
        config.put(MqttClientLink.PASSWORD, password);
        return config;
    }
}
