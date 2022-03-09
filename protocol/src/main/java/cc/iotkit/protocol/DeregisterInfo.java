package cc.iotkit.protocol;

import lombok.Data;


/**
 * 注销信息
 */
@Data
public class DeregisterInfo {

    private String productKey;

    private String deviceName;

    private boolean cascade;
}
