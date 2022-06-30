package cc.iotkit.model.device;

import cc.iotkit.model.Owned;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 设备分组
 */
@Data
@Document(indexName = "device_group")
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
    @Field(type = FieldType.Date)
    private long createAt;

}
