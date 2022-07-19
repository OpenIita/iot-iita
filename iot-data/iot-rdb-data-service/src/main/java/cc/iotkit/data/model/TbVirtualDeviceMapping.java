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
@Table(name = "virtual_device_mapping")
public class TbVirtualDeviceMapping {

    @Id
    private String id;

    private String virtualId;

    private String deviceId;

}
