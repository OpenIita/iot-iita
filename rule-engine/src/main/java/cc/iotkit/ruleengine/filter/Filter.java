package cc.iotkit.ruleengine.filter;

import cc.iotkit.model.device.message.ThingModelMessage;

import java.util.List;

/**
 * 场景过滤器
 */
public interface Filter<T> {

    String getType();

    List<T> getConditions();

    void init();

    boolean execute(ThingModelMessage msg);
}
