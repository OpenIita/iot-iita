/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.converter;

import cc.iotkit.common.thing.ThingService;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.script.IScriptEngine;
import cc.iotkit.script.ScriptEngineFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class JavaScriptConverter implements IConverter {

    private IScriptEngine scriptEngine = ScriptEngineFactory.getScriptEngine("js");

    @SneakyThrows
    public void setScript(String script) {
        scriptEngine.setScript(script);
    }

    @SneakyThrows
    public ThingModelMessage decode(DeviceMessage msg) {
        return scriptEngine.invokeMethod(new TypeReference<>() {
        }, "decode", msg);
    }

    @SneakyThrows
    @Override
    public DeviceMessage encode(ThingService<?> service, Device device) {
        return scriptEngine.invokeMethod(new TypeReference<>() {
        }, "encode", service, device);
    }

    @Override
    public void putScriptEnv(String key, Object value) {
        scriptEngine.putScriptEnv(key, value);
    }
}
