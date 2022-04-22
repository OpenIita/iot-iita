package cc.iotkit.model.device;

import cc.iotkit.model.Owned;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceInfo implements Owned {

    @Id
    private String id;

    private String deviceId;

    private String productKey;

    private String deviceName;

    private String model;

    private String parentId;

    /**
     * 所属平台用户ID
     */
    private String uid;

    /**
     * 关联子用户ID列表
     */
    private List<String> subUid;

    private State state = new State();

    private Map<String, Object> property;

    /**
     * 设备标签
     */
    private Map<String, Tag> tag;

    private Long createAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class State {

        private boolean online;

        private Long onlineTime;

        private Long offlineTime;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tag {
        private String id;
        private String name;
        private Object value;
    }

}
