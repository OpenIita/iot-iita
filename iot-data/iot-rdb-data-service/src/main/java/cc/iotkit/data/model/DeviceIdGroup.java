package cc.iotkit.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceIdGroup {

    private String id;
    private String deviceId;
    private String name;

}
