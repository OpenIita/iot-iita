package cc.iotkit.model.space;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class SpaceDevice {

    @Id
    private String id;

    /**
     * 关联用户id
     */
    private String uid;

    /**
     * 分享的用户id
     */
    private String sharedUid;

    /**
     * 空间中的设备id
     */
    private String deviceId;

    /**
     * 空间中的设备名称
     */
    private String name;

    /**
     * 所属家庭Id
     */
    private String homeId;

    /**
     * 空间id
     */
    private String spaceId;

    /**
     * 空间名称
     */
    private String spaceName;
}
