package cc.iotkit.comp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 注册信息
 */
@Slf4j
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

    public RegisterInfo(String productKey, String deviceName, String subProductKey, String subDeviceName) {
        this.productKey = productKey;
        this.deviceName = deviceName;
        if (subProductKey != null && subDeviceName != null) {
            SubDevice subDevice = new SubDevice(subProductKey, subDeviceName, null, null);
            subDevices = new ArrayList<>();
            subDevices.add(subDevice);
        }
    }

    public static RegisterInfo from(Map map) {
        RegisterInfo bean = new RegisterInfo();
        try {
            BeanUtils.populate(bean, map);
            List<SubDevice> subDevices = new ArrayList<>();
            List<Object> sourceSubDevices = (List<Object>) map.get("subDevices");
            if (sourceSubDevices == null) {
                return bean;
            }
            for (Object sourceSubDevice : sourceSubDevices) {
                SubDevice subDevice = new SubDevice();
                BeanUtils.populate(subDevice, (Map<String, ? extends Object>) sourceSubDevice);
                subDevices.add(subDevice);
            }
            bean.setSubDevices(subDevices);
        } catch (Throwable e) {
            log.error("parse bean from map error", e);
            return null;
        }
        return bean;
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