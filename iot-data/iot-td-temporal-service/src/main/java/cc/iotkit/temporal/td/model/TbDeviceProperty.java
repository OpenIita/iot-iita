package cc.iotkit.temporal.td.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbDeviceProperty {

    private Long time;

    private String deviceId;

    private String name;

    private Object value;

}
