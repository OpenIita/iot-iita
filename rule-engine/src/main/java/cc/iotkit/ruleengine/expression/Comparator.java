package cc.iotkit.ruleengine.expression;

import java.util.Map;

public interface Comparator {

    String getName();

    String getScript();

    Map<String, Object> getData(Object left, Object right);

}
