package cc.iotkit.ruleengine.filter;

import cc.iotkit.dao.DeviceCache;
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

    private DeviceCache deviceCache;

    public DeviceCondition clone() {
        DeviceCondition con = new DeviceCondition();
        con.setDevice(device);
        con.setType(type);
        con.setIdentifier(identifier);
        con.setValue(value);
        con.setComparator(comparator);
        con.setDeviceCache(deviceCache);
        return con;
    }

    public boolean matches() {
        DeviceInfo deviceInfo;
        String[] pkDn = device.split("/");
        if (pkDn.length < 2) {
            //用deviceId取
            deviceInfo = deviceCache.get(device);
        } else {
            //用pk/dn取
            deviceInfo = deviceCache.getDeviceInfo(pkDn[0], pkDn[1]);
        }
        Object left = null;
        if ("property".equals(type)) {
            Map<String, Object> properties = deviceInfo.getProperty();
            left = properties.get(identifier);
        } else if ("state".equals(type)) {
            DeviceInfo.State state = deviceInfo.getState();
            left = state != null && state.isOnline();
        } else if ("tag".equals(type)) {
            //取设备标签判断
            Map<String, DeviceInfo.Tag> tags = deviceInfo.getTag();
            if (tags == null) {
                left = null;
            } else {
                DeviceInfo.Tag tag = tags.get(identifier);
                if (tag == null) {
                    left = null;
                } else {
                    //设备标签值
                    left = tag.getValue();
                }
            }
        }
        return Expression.eval(comparator, left, value);
    }
}
