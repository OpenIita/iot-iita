package cc.iotkit.model.device;

import cc.iotkit.model.Owned;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceAclInfo {

    @Id
    private String id;

    private String deviceId;

    private String productKey;

    private String deviceName;

    private String topic;

    private String access;

    private String parentId;

}
