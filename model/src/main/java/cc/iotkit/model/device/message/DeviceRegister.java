package cc.iotkit.model.device.message;

import lombok.Data;

@Data
public class DeviceRegister {

    private String id;

    private String productKey;

    private String deviceName;

    private String model;

}
