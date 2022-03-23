package cc.iotkit.comps.model;

import lombok.Data;

@Data
public class DeviceState {

    public static final String STATE_ONLINE = "online";
    public static final String STATE_OFFLINE = "offline";

    private String productKey;

    private String deviceName;

    private String state;

}
