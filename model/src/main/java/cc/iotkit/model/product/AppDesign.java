package cc.iotkit.model.product;

import cc.iotkit.model.Owned;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class AppDesign implements Owned {

    private String id;

    private String productKey;

    private String template;

    private String uid;

    private Boolean state;

    private Long modifyAt;

}
