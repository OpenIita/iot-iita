package cc.iotkit.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceMessage {

    private String productKey;

    private String deviceName;

    private String mid;

    private String content;

}
