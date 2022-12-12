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
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.model.product.ProductModel;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import javax.script.ScriptEngineManager;
import java.util.Map;

@Slf4j
@Data
public class JsScripter implements IScripter {

    private ProductModel model;

    private final NashornScriptEngine engine = (NashornScriptEngine) (new ScriptEngineManager()).getEngineByName("nashorn");

    private Object scriptObj;

    public JsScripter(ProductModel model) {
        this.model = model;
    }

    @Override
    public void setScript(String script) {
        try {
            scriptObj = engine.eval(String.format("new (function () {\n%s})()", script));
        } catch (Throwable e) {
            throw new RuntimeException("init script error", e);
        }
    }

    public ThingModelMessage decode(TransparentMsg msg) {
        try {
            ScriptObjectMirror result = (ScriptObjectMirror) engine.invokeMethod(scriptObj, "decode", msg);
            ThingModelMessage message = new ThingModelMessage();
            BeanUtils.populate(message, result);
            return message;
        } catch (Throwable e) {
            log.error("invoke decode script error", e);
            return null;
        }
    }

    public TransparentMsg encode(ThingService<?> service) {
        try {
            ScriptObjectMirror result = (ScriptObjectMirror) engine.invokeMethod(scriptObj, "encode", service);
            Map map = (Map) JsonUtil.toObject(result);
            TransparentMsg message = new TransparentMsg();
            BeanUtils.populate(message, map);
            message.setProductKey(model.getProductKey());
            message.setModel(model.getModel());
            message.setDeviceName(service.getDeviceName());
            return message;
        } catch (Throwable e) {
            log.error("invoke encode script error", e);
            return null;
        }
    }
}
