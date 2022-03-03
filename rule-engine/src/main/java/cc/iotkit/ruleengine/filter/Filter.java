package cc.iotkit.ruleengine.filter;

import java.util.List;

/**
 * 场景过滤器
 */
public interface Filter<T> {

    String getType();

    List<T> getConditions();

    boolean execute();
}
