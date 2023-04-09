/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.ruleengine.action;

import cc.iotkit.data.IDeviceInfoData;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.script.IScriptEngine;
import cc.iotkit.script.ScriptEngineFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Data
public class ScriptService {

    private IScriptEngine scriptEngine = ScriptEngineFactory.getScriptEngine("js");

    private String script;

    private IDeviceInfoData deviceInfoData;

    public Map execScript(ThingModelMessage msg) {
        try {
            scriptEngine.setScript(script);
            //取设备信息
            DeviceInfo deviceInfo = deviceInfoData.findByDeviceId(msg.getDeviceId());

            //执行转换脚本
            return scriptEngine.invokeMethod(new TypeReference<>() {
            }, "translate", msg, deviceInfo);
        } catch (Throwable e) {
            log.error("run script error", e);
            return null;
        }
    }
}
