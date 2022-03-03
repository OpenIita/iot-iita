package cc.iotkit.ruleengine.listener;

import java.util.List;
import java.util.Map;

public interface Listener<T> {

    String getType();

    String getTopic();

    List<T> getConditions();

    boolean execute(String topic, Map<?, ?> params);
}
