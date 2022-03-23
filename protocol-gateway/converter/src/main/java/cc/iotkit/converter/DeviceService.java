package cc.iotkit.converter;

import lombok.Data;

@Data
public class DeviceService<T> {

    private String mid;

    private String productKey;

    private String deviceName;

    private String type;

    private String identifier;

    private T params;

}
