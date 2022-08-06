package cc.iotkit.temporal.td.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbThingModelMessage {

    private Long time;

    private String mid;

    private String deviceId;

    private String productKey;

    private String deviceName;

    private String uid;

    private String type;

    private String identifier;

    private int code;

    private String data;

    private Long reportTime;

}
