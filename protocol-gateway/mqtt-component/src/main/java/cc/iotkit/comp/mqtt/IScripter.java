package cc.iotkit.comp.mqtt;

import cc.iotkit.converter.DeviceMessage;
import cc.iotkit.converter.ThingService;
import cc.iotkit.model.device.message.ThingModelMessage;

public interface IScripter {

    void setScript(String script);

    /**
     * 透传解码
     */
    ThingModelMessage decode(TransparentMsg msg);

    /**
     * 透传编码
     */
    TransparentMsg encode(ThingService<?> service);
}
