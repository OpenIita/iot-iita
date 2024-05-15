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
package cc.iotkit.ruleengine.action.alert;

import cc.iotkit.common.thing.ThingModelMessage;
import cc.iotkit.message.model.Message;
import cc.iotkit.message.service.MessageService;
import cc.iotkit.ruleengine.action.ScriptService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author sjg
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class AlertService extends ScriptService {

    private Message message;

    private MessageService messageService;

    @SneakyThrows
    public String execute(ThingModelMessage msg) {
        //执行转换脚本
        Map<String, Object> result = execScript(new TypeReference<>() {
        }, msg);
        if (result == null) {
            log.warn("execScript result is null");
            return "execScript result is null";
        }
        message.setParam(result);
        messageService.sendMessage(message);
        return "ok";
    }
}
