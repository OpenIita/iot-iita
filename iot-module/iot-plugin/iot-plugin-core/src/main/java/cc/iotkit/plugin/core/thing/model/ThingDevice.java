package cc.iotkit.plugin.core.thing.model;

import lombok.Data;

/**
 * 设备信息
 *
 * @author sjg
 */
@Data
public class ThingDevice {

    private String deviceId;

    /**
     * 产品key
     */
    private String productKey;

    /**
     * 设备dn
     */
    private String deviceName;

    /**
     * 设备型号
     */
    private String model;

    /**
     * 设备密钥
     */
    private String secret;
}
