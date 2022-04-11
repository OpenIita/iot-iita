package cc.iotkit.comp.model;

import cc.iotkit.common.utils.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Data
@Slf4j
public class DeviceState {

    public static final String STATE_ONLINE = "online";
    public static final String STATE_OFFLINE = "offline";

    private String productKey;

    private String deviceName;

    private String state;

    private Parent parent;

    public static DeviceState from(Map map) {
        return JsonUtil.parse(JsonUtil.toJsonString(map), DeviceState.class);
    }

    @Data
    public static class Parent {
        private String productKey;
        private String deviceName;
    }
}
