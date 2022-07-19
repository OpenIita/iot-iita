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

public class JsScripter implements IScripter {

    @Override
    public void setScript(String script) {
    }

    public ThingModelMessage decode(TransparentMsg msg) {
        return null;
    }

    public TransparentMsg encode(ThingService<?> service) {
        return null;
    }
}
