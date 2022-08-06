package cc.iotkit.temporal.td.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbVirtualDeviceLog {

    private Long time;

    private String virtualDeviceId;

    private String virtualDeviceName;

    private int deviceTotal;

    private String result;

}
