package cc.iotkit.ruleengine.listener;

import cc.iotkit.model.device.message.ThingModelMessage;

import java.util.List;
import java.util.Map;

public interface Listener<T> {

    String getType();

    List<T> getConditions();

    boolean execute(ThingModelMessage msg);
}
