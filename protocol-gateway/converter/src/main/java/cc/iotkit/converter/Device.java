package cc.iotkit.converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    private String deviceId;

    private String model;

    /**
     * 是否透传
     */
    private Boolean transparent;

}
