package cc.iotkit.model.device.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import cc.iotkit.model.mq.Request;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class DeviceEvent {

    @Id
    private String id;

    private String deviceId;

    private String identifier;

    private Request<?> request;

    private String type;

    private Long createAt;

}
