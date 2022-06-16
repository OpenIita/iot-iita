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
public class DeviceInfo implements Owned {

    @Id
    private String id;

    private String deviceId;

    /**
     * 产品key
     */
    private String productKey;

    private String deviceName;

    /**
     * 设备型号
     */
    private String model;

    /**
     * 设备密钥
     */
    private String secret;

    private String parentId;

    /**
     * 所属平台用户ID
     */
    private String uid;

    /**
     * 关联子用户ID列表
     */
    private List<String> subUid = new ArrayList<>();

    /**
     * 设备在离线状态
     */
    private State state = new State();

    /**
     * 设备属性
     */
    private Map<String, Object> property = new HashMap<>();

    /**
     * 设备标签
     */
    private Map<String, Tag> tag = new HashMap<>();

    /**
     * 设备所属分组
     */
    private Map<String, Group> group = new HashMap<>();

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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Group {
        private String id;
        private String name;
    }

}
