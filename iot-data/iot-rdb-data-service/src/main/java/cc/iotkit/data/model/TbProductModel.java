package cc.iotkit.data.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "product_model")
public class TbProductModel {

    @Id
    private String id;

    /**
     * 型号在所有产品中唯一
     */
    private String model;

    private String name;

    private String productKey;

    private String type;

    private String script;

    /**
     * 脚本状态，只有发布状态才生效
     */
    private String state;

    private Long modifyAt;
}
