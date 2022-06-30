package cc.iotkit.manager.model.query;

import lombok.Data;

@Data
public class DeviceQuery {

    private String productKey;

    private String keyword;

    private String group;

    private String state;

}
