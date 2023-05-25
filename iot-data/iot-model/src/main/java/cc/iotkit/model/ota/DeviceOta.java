package cc.iotkit.model.ota;

import lombok.Data;

/**
 * @Author: 石恒
 * @Date: 2023/5/25 22:21
 * @Description:
 */
@Data
public class DeviceOta {
    private String currentVersion;
    private String deviceId;
}
