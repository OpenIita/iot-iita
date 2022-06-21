/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comp.mqtt;

import cc.iotkit.common.thing.ThingService;
import cc.iotkit.model.device.message.ThingModelMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.script.LuaScriptEngine;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class LuaScripter implements IScripter {

    private final LuaScriptEngine engine = (LuaScriptEngine) (
            new ScriptEngineManager().getEngineByName("luaj"));

    private LuaValue decoder;
    private LuaValue encoder;

    @Override
    public void setScript(String script) {
        try {
            CompiledScript compiledScript = ((Compilable) engine).compile(script);
            SimpleBindings bindings = new SimpleBindings();
            compiledScript.eval(bindings);
            decoder = (LuaValue) bindings.get("decode");
            encoder = (LuaValue) bindings.get("encode");
        } catch (Throwable e) {
            log.error("compile script error", e);
        }
    }

    public ThingModelMessage decode(TransparentMsg msg) {
        try {
            LuaTable table = new LuaTable();
            table.set("model", msg.getModel());
            table.set("mac", msg.getMac());
            table.set("data", msg.getData());
            Map result = (Map) parse(decoder.call(table));
            ThingModelMessage modelMessage = new ThingModelMessage();
            BeanUtils.populate(modelMessage, result);

            modelMessage.setProductKey(msg.getProductKey());
            modelMessage.setDeviceName(msg.getMac());
            return modelMessage;
        } catch (Throwable e) {
            log.error("execute decode script error", e);
        }
        return null;
    }

    public TransparentMsg encode(ThingService<?> service) {
        try {
            LuaTable table = new LuaTable();
            table.set("identifier", service.getIdentifier());
            table.set("type", service.getType());
            table.set("productKey", service.getProductKey());
            table.set("deviceName", service.getDeviceName());
            table.set("mid", service.getMid());
            Object params = service.getParams();
            LuaTable tableParams = new LuaTable();
            if (params instanceof Map) {
                ((Map<?, ?>) params).forEach((key, val) -> tableParams.set(key.toString(), parse(val)));
            }
            table.set("params", tableParams);
            LuaValue result = encoder.call(table);
            Map map = (Map) parse(result);
            TransparentMsg message = new TransparentMsg();
            BeanUtils.populate(message, map);
            return message;
        } catch (Throwable e) {
            log.error("execute encode script error", e);
        }
        return null;
    }

    private Object parse(LuaValue value) {
        String type = value.typename();
        switch (type) {
            case "string":
                return value.toString();
            case "number":
            case "int":
                return value.toint();
            case "table":
                Map<String, Object> data = new HashMap<>();
                LuaTable table = (LuaTable) value;
                int arrLen = table.rawlen();
                if (arrLen > 0) {
                    //数组转换
                    List<Object> list = new ArrayList<>();
                    for (LuaValue key : table.keys()) {
                        list.add(parse(table.get(key)));
                    }
                    return list;
                } else {
                    //map转换
                    for (LuaValue key : table.keys()) {
                        data.put(key.toString(), parse(table.get(key)));
                    }
                }
                return data;
        }
        return null;
    }

    private LuaValue parse(Object value) {
        if (value instanceof String) {
            return LuaValue.valueOf(value.toString());
        }
        if (value instanceof Integer) {
            return LuaValue.valueOf((Integer) value);
        }
        return new LuaTable();
    }

}
