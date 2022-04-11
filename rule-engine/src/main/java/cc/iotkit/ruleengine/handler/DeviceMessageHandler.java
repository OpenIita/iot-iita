package cc.iotkit.ruleengine.handler;

import cc.iotkit.model.device.message.ThingModelMessage;

public interface DeviceMessageHandler {

    void handle(ThingModelMessage message);

}
