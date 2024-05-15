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
package cc.iotkit.ruleengine.action;

import cc.iotkit.common.thing.ThingModelMessage;
import cc.iotkit.data.manager.IDeviceInfoData;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.script.IScriptEngine;
import cc.iotkit.script.ScriptEngineFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
public class ScriptService {

    private IScriptEngine scriptEngine = ScriptEngineFactory.getScriptEngine("js");

    private String script;

    private IDeviceInfoData deviceInfoData;

    public void setScript(String script) {
        this.script=script;
        scriptEngine.setScript(script);
    }

    public <T> T execScript(TypeReference<T> type, ThingModelMessage msg) {
        try {
            //取设备信息
            DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(msg.getDeviceId());

            //执行转换脚本
            return scriptEngine.invokeMethod(type, "translate", msg, deviceInfo);
        } catch (Throwable e) {
            log.error("run script error", e);
            return null;
        }
    }
}
