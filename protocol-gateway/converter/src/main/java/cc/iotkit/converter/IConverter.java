package cc.iotkit.converter;

import cc.iotkit.model.device.message.ThingModelMessage;

public interface IConverter {

    void setScript(String script);

    ThingModelMessage decode(DeviceMessage msg);

    DeviceMessage encode(ThingService<?> service, Device device);

    void putScriptEnv(String key, Object value);
}
