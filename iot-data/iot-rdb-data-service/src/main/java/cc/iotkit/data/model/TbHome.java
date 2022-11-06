package cc.iotkit.data.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "home")
public class TbHome {

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

    /**
     * 是否为用户当前使用的家庭
     */
    private Boolean current;

}
