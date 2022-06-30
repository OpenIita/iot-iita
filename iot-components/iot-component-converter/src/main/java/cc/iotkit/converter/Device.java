package cc.iotkit.converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    private String deviceId;

    private String model;

    private Map<String, Object> property = new HashMap<>();

    private Map<String, Object> tag = new HashMap<>();

    /**
     * 是否透传
     */
    private Boolean transparent;

}
