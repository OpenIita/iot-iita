package cc.iotkit.data.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "product")
public class TbProduct {

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
     * 是否透传,true/false
     */
    private String transparent;

    private Long createAt;

}