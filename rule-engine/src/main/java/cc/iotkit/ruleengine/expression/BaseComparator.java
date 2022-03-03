package cc.iotkit.ruleengine.expression;


import java.util.HashMap;
import java.util.Map;

public abstract class BaseComparator implements Comparator {

    @Override
    public Map<String, Object> getData(Object left, Object right) {
        Map<String, Object> data = new HashMap<>();
        data.put("a", left);
        data.put("b", right);
        return data;
    }
}
