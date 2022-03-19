package cc.iotkit.ruleengine.filter;

import cc.iotkit.dao.DeviceCache;
import lombok.Data;

import java.util.List;

@Data
public class DeviceFilter implements Filter<DeviceCondition> {

    public static String TYPE = "device";

    private String type;

    private List<DeviceCondition> conditions;

    private DeviceCache deviceCache;

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean execute() {
        for (DeviceCondition condition : getConditions()) {
            condition.setDeviceCache(deviceCache);
            if (!condition.matches()) {
                return false;
            }
        }
        return true;
    }

}
