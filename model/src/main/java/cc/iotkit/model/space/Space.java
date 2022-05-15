package cc.iotkit.model.space;

import cc.iotkit.model.Owned;
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
public class Space implements Owned {

    @Id
    private String id;

    /**
     * 关联家庭id
     */
    private String homeId;

    /**
     * 关联用户id
     */
    private String uid;

    /**
     * 空间名称
     */
    private String name;

    /**
     * 设备数量
     */
    private Integer deviceNum;

    private Long createAt;
}
