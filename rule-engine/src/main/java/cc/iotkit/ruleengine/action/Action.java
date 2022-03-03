package cc.iotkit.ruleengine.action;

import java.util.List;

public interface Action<T> {

    String getType();

    List<T> getServices();

    void execute();
}
