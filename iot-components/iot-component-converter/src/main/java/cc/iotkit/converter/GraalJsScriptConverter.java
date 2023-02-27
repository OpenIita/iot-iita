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
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.model.device.message.ThingModelMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.graalvm.polyglot.*;

@Slf4j
@Data
public class GraalJsScriptConverter implements IConverter {


    private final Context context = Context.newBuilder("js").allowHostAccess(true).build();


    private Value decoder;
    private Value encoder;

    public void setScript(String script) {

        Value myFunctions = context.eval("js",String.format("new (function () {\n%s})()", script));
        // 调用JavaScript函数
        decoder = myFunctions.getMember("decode");
        encoder = myFunctions.getMember("encode");

    }

    public ThingModelMessage decode(DeviceMessage msg) {
        try {
//            String msgJson = JsonUtil.toJsonString(msg);
            Value rst = decoder.execute(msg);
            ThingModelMessage modelMessage = new ThingModelMessage();
            CovertUtils.copyProperties(modelMessage, rst);
            return modelMessage;
        } catch (Throwable e) {
            log.error("execute decode script error", e);
        }
        return null;
    }


    @Override
    public DeviceMessage encode(ThingService<?> service, Device device) {
        try {
            Value rst = encoder.execute(service,device);
            DeviceMessage modelMessage = rst.as(DeviceMessage.class);
            return modelMessage;
        } catch (Throwable e) {
            log.error("execute encode script error", e);
        }
        return null;
    }

    @Override
    public void putScriptEnv(String key, Object value) {
        context.getBindings("js").putMember(key, value);
    }
}
