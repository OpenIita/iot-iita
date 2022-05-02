package cc.iotkit.model.product;

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
public class Product implements Owned {

    @Id
    private String id;

    private String name;

    private String category;

    private Integer nodeType;

    /**
     * 所属平台用户ID
     */
    private String uid;

    private String img;

    /**
     * 是否透传
     */
    private Boolean transparent;

    private Long createAt;

    public boolean isTransparent() {
        return transparent != null && transparent;
    }
}