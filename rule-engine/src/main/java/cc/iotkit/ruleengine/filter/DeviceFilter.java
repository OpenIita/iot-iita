package cc.iotkit.ruleengine.filter;

import cc.iotkit.dao.DeviceCache;
import cc.iotkit.model.device.message.ThingModelMessage;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
    public void init() {
    }

    @Override
    public boolean execute(ThingModelMessage msg) {
        for (DeviceCondition condition : getConditions()) {
            DeviceCondition con = condition.clone();
            //未指定device，使用消息中的deviceId
            if (StringUtils.isBlank(con.getDevice())) {
                con.setDevice(msg.getDeviceId());
            }

            con.setDeviceCache(deviceCache);
            if (!con.matches()) {
                return false;
            }
        }
        return true;
    }

}
