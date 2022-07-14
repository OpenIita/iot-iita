package cc.iotkit.data.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "device_sub_user")
public class TbDeviceSubUser {

    @Id
    private String id;

    private String deviceId;

    private String uid;

}
