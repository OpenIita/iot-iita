package cc.iotkit.comp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveResult {

    private String productKey;

    private String deviceName;

    private Object data;

}
