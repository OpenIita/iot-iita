package cc.iotkit.model.device.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Map;

/**
 * 物模型消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "thing_model_messages")
public class ThingModelMessage {

    public static final String TYPE_PROPERTY="property";
    public static final String TYPE_EVENT="event";
    public static final String TYPE_SERVICE="service";

    public static final String ID_PROPERTY_GET="get";
    public static final String ID_PROPERTY_SET="set";

    @Id
    private String mid;

    private String productKey;

    private String deviceName;

    /**
     * 消息类型
     * property:属性
     * event:事件
     * service:服务
     */
    private String type;

    private String identifier;

    private Map<String,Object> data;

    /**
     * 时间戳，设备上的事件或数据产生的本地时间
     */
    @Field(type = FieldType.Date)
    private Long occur;

    /**
     * 消息上报时间
     */
    @Field(type = FieldType.Date)
    private Long time;
}
