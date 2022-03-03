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
public class Home {

    @Id
    private String id;

    /**
     * 家庭名称
     */
    private String name;

    /**
     * 家庭地址
     */
    private String address;

    /**
     * 关联用户id
     */
    private String uid;

    /**
     * 空间数量
     */
    private Integer spaceNum;

    /**
     * 设备数量
     */
    private Integer deviceNum;
}
