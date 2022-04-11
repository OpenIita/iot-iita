package cc.iotkit.ruleengine.listener;

import cc.iotkit.model.device.message.ThingModelMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class DeviceListener implements Listener<DeviceCondition> {

    public static final String TYPE = "device";

    private String type;

    private List<DeviceCondition> conditions;

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean execute(ThingModelMessage message) {
        String identifier = message.getIdentifier();
        Map<String, Object> mapData = message.dataToMap();
        for (DeviceCondition condition : this.conditions) {
            if (condition.matches(message.getType(), identifier, mapData)) {
                return true;
            }
        }

        return false;
    }

}
