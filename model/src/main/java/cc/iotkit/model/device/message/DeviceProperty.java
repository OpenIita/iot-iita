package cc.iotkit.model.device.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "device_property")
public class DeviceProperty {

    @Id
    private String mid;

    private String deviceId;

    private String name;

    private Object value;

    @Field(type = FieldType.Date)
    private Long time;

}
