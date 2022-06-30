package cc.iotkit.model.device.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 设备上报消息-用于统计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "device_report")
public class DeviceReport {

    @Id
    private String id;

    private String deviceId;

    private String productKey;

    private String deviceName;

    /**
     * 设备所属用户
     */
    private String uid;

    /**
     * 消息类型
     * lifetime:生命周期
     * state:状态
     * property:属性
     * event:事件
     * service:服务
     */
    private String type;

    private String identifier;

    /**
     * 消息状态码
     */
    private int code;

    /**
     * 消息上报时间
     */
    @Field(type = FieldType.Date)
    private Long time;

}
