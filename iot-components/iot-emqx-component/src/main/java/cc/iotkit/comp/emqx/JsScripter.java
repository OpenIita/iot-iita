/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comp.emqx;

import cc.iotkit.common.thing.ThingService;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.model.product.ProductModel;
import cc.iotkit.script.IScriptEngine;
import cc.iotkit.script.ScriptEngineFactory;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class JsScripter implements IScripter {

    private ProductModel model;

    private Object scriptObj;

    private IScriptEngine scriptEngine = ScriptEngineFactory.getScriptEngine("js");

    public JsScripter(ProductModel model) {
        this.model = model;
    }

    @SneakyThrows
    @Override
    public void setScript(String script) {
        scriptEngine.setScript(script);
    }

    @SneakyThrows
    public ThingModelMessage decode(TransparentMsg msg) {
        return scriptEngine.invokeMethod(ThingModelMessage.class, "decode", msg).get(0);
    }

    @SneakyThrows
    public TransparentMsg encode(ThingService<?> service) {
        return scriptEngine.invokeMethod(TransparentMsg.class, "encode", service).get(0);
    }
}
