package cc.iotkit.comp.model;

import lombok.Data;

@Data
public class DeviceMessage {

    private String productKey;

    private String deviceName;

    private String mid;

    private String content;
}
