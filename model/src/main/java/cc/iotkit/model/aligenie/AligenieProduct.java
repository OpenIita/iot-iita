package cc.iotkit.model.aligenie;

import cc.iotkit.model.Owned;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 天猫精灵产品
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class AligenieProduct implements Owned {

    @Id
    private String productId;
    private String deviceType;
    private String brand;
    private String model;
    private String icon;
    private List<Property> properties;
    private List<String> actions;

    /**
     * 对应系统中的产品pk
     */
    private String productKey;
    /**
     * 物模型转换配置
     */
    private String transform;

    /**
     * 所属系统账户
     */
    private String uid;

    private Long createAt;

    @Override
    public String getId() {
        return productId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Property {
        private String name;
        private String value;
    }
}
