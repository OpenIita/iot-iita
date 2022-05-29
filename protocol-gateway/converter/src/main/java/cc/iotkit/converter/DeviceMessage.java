package cc.iotkit.converter;

import lombok.Data;

@Data
public class DeviceMessage {

    private String productKey;

    private String deviceName;

    private String mid;

    private Object content;
}
