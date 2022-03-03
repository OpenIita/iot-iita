package cc.iotkit.ruleengine.listener;

import cc.iotkit.ruleengine.expression.Expression;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DeviceCondition {

    private String type;

    private String identifier;

    private List<Parameter> parameters;

    public boolean matches(String type, String identifier, Map<?, ?> parameter) {
        if (!this.type.equals(type)) {
            return false;
        }
        //通配规则，不需要判断其它条件
        if (this.identifier.endsWith(":*")) {
            return true;
        }
        if (!this.identifier.equals(identifier)) {
            return false;
        }
        for (Parameter p : this.parameters) {
            if (!p.matches(parameter)) {
                return false;
            }
        }
        return true;
    }

    @Data
    public static class Parameter {
        private String identifier;
        private Object value;
        private String comparator;

        public boolean matches(Map<?, ?> parameter) {
            Object left = parameter.get(identifier);
            if (left == null) {
                return false;
            }
            return Expression.eval(comparator, left, value);
        }
    }
}
