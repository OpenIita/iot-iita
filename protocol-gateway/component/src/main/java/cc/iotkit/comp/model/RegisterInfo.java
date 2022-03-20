package cc.iotkit.comp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 注册信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterInfo {

    private String productKey;

    private String deviceName;

    private String model;

    private Map<String, Object> tag;

    private List<SubDevice> subDevices;

    public RegisterInfo(String productKey, String deviceName, String model) {
        this.productKey = productKey;
        this.deviceName = deviceName;
        this.model = model;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubDevice {

        private String productKey;

        private String deviceName;

        private String model;

        private Map<String, Object> tag;
    }
}
