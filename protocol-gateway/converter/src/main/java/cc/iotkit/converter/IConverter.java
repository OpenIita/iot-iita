package cc.iotkit.converter;

import cc.iotkit.model.device.message.ThingModelMessage;

public interface IConverter {

    void setScript(String script);

    ThingModelMessage decode(String msg);

    String encode(DeviceService<?> service, Device device);

}
