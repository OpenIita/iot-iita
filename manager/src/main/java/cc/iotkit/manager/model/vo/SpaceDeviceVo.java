package cc.iotkit.manager.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class SpaceDeviceVo {

    private String id;

    /**
     * 关联用户id
     */
    private String uid;

    /**
     * 空间中的设备id
     */
    private String deviceId;

    /**
     * 空间中的设备名称
     */
    private String name;

    /**
     * 设备DN
     */
    private String deviceName;

    /**
     * 设备图片
     */
    private String picUrl;

    /**
     * 空间ID
     */
    private String spaceId;

    /**
     * 空间名称
     */
    private String spaceName;

    /**
     * 设备状态
     */
    private Boolean online;

    /**
     * 设备属性
     */
    private Map<String, Object> property = new HashMap<>();

    /**
     * 产品key
     */
    private String productKey;

}
