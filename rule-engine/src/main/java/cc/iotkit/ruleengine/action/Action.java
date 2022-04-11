package cc.iotkit.ruleengine.action;

import cc.iotkit.model.device.message.ThingModelMessage;

import java.util.List;

public interface Action<T> {

    String getType();

    List<T> getServices();

    void execute(ThingModelMessage msg);
}
