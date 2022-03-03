package cc.iotkit.ruleengine.filter;

import cc.iotkit.dao.DeviceDao;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.ruleengine.expression.Expression;
import lombok.Data;

import java.util.Map;

@Data
public class DeviceCondition {

    private String device;

    private String type;

    private String identifier;

    private Object value;

    private String comparator;

    private DeviceDao deviceDao;

    public boolean matches() {
        String[] pkDn = device.split("/");
        DeviceInfo deviceInfo = deviceDao.getByPkAndDn(pkDn[0], pkDn[1]);
        Object left = null;
        if ("property".equals(type)) {
            Map<String, Object> properties = deviceInfo.getProperty();
            left = properties.get(identifier);
        } else if ("state".equals(type)) {
            DeviceInfo.State state = deviceInfo.getState();
            left = state != null && state.getOnline();
        }
        return Expression.eval(comparator, left, value);
    }
}
