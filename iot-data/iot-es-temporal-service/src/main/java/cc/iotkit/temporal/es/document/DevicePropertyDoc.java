package cc.iotkit.temporal.es.document;

import cc.iotkit.model.device.message.DeviceProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "device_property")
public class DevicePropertyDoc {

    @Id
    private String id;

    private String deviceId;

    private String name;

    private Object value;

    @Field(type = FieldType.Date)
    private Long time;

    public DeviceProperty de() {
        return new DeviceProperty(id, deviceId, name, value, time);
    }

    public DevicePropertyDoc(DeviceProperty raw) {
        this.id = raw.getId();
        this.deviceId = raw.getDeviceId();
        this.name = raw.getName();
        this.value = raw.getValue();
        this.time = raw.getTime();
    }
}
