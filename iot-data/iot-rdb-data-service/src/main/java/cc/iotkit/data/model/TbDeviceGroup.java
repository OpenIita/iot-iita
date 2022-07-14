package cc.iotkit.data.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "device_group")
public class TbDeviceGroup {

    @Id
    private String id;

    private String name;

    /**
     * 所属用户
     */
    private String uid;

    /**
     * 分组说明
     */
    private String remark;

    /**
     * 设备数量
     */
    private int deviceQty;

    /**
     * 创建时间
     */
    private long createAt;

}
