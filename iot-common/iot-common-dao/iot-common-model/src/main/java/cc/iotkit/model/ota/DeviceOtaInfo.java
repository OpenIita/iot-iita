package cc.iotkit.model.ota;

import cc.iotkit.model.Id;
import lombok.*;

/**
 * @Author: 石恒
 * @Date: 2023/6/15 22:00
 * @Description:
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceOtaInfo implements Id<Long> {

    private Long id;

    private Integer step;

    private String taskId;

    private String desc;

    private String version;

    private String module;

    private String deviceId;

    private String productKey;

    private String deviceName;
}
