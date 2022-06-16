package cc.iotkit.model.device;

import cc.iotkit.model.Owned;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 设备分组
 */
@Data
@Document
public class DeviceGroup implements Owned {

    /**
     * 分组id
     */
    @Id
    private String id;

    /**
     * 分组名称
     */
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
