package cc.iotkit.model.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "product_model")
public class ProductModel {

    public static final String TYPE_JS = "JavaScript";
    public static final String TYPE_LUA = "LuaScript";

    public static final String STATE_DEV = "dev";
    public static final String STATE_PUBLISH = "publish";

    /**
     * 型号在所有产品中唯一
     */
    @Id
    private String model;

    private String name;

    private String productKey;

    private String type;

    private String script;

    /**
     * 脚本状态，只有发布状态才生效
     */
    private String state;

    @Field(type = FieldType.Date)
    private Long modifyAt;

}
