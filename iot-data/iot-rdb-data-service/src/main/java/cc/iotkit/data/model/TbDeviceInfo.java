package cc.iotkit.data.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "device_info")
public class TbDeviceInfo {

    @javax.persistence.Id
    private String id;

    private String deviceId;

    private String productKey;

    private String deviceName;

    private String model;

    private String secret;

    private String parentId;

    private String uid;

    private String state;

    private String longitude;

    private String latitude;

    private Long onlineTime;

    private Long offlineTime;

    private Long createAt;

}
