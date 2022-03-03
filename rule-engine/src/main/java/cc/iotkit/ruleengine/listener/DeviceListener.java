package cc.iotkit.ruleengine.listener;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class DeviceListener implements Listener<DeviceCondition> {

    public static final String TYPE = "device";

    private String type;

    private String topic;

    private List<DeviceCondition> conditions;

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean execute(String topic, Map<?, ?> params) {
        String[] parts = topic.split("/");
        if (parts.length < 2) {
            log.error("topic:{} is not Satisfiable", topic);
            return false;
        }

        String identifier = parts[parts.length - 1];
        for (DeviceCondition condition : this.conditions) {
            if (!condition.matches(condition.getType(), identifier, params)) {
                return false;
            }
        }

        return true;
    }

}
