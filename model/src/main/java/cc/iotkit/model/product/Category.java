package cc.iotkit.model.product;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Category {
    @Id
    private String id;

    private String name;

    private Long createAt;
}
