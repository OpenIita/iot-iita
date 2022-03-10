package cc.iotkit.protocol;

import lombok.Data;

/**
 * 设备消息
 */
@Data
public class DeviceMessage {

    public static final String TYPE_REQUEST = "request";
    public static final String TYPE_ACK = "ack";

    private String productKey;

    private String deviceName;

    private String type;

    private String mid;

    private String content;

}
