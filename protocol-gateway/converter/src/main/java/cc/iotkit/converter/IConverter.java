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

public interface IConverter {

    void setScript(String script);

    ThingModelMessage decode(DeviceMessage msg);

    DeviceMessage encode(ThingService<?> service, Device device);

    void putScriptEnv(String key, Object value);
}
