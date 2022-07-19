package cc.iotkit.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "device_group_mapping")
public class TbDeviceGroupMapping {

    @Id
    private String id;

    private String deviceId;

    private String groupId;

}
