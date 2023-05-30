package cc.iotkit.data.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "home")
@ApiModel(value = "家庭信息")
public class TbHome {

    @Id
    @ApiModelProperty(value = "家庭id")
    private String id;

    /**
     * 家庭名称
     */
    @ApiModelProperty(value = "家庭名称")
    private String name;

    /**
     * 家庭地址
     */
    @ApiModelProperty(value = "家庭地址")
    private String address;

    /**
     * 关联用户id
     */
    @ApiModelProperty(value = "关联用户id")
    private String uid;

    /**
     * 空间数量
     */
    @ApiModelProperty(value = "空间数量")
    private Integer spaceNum;

    /**
     * 设备数量
     */
    @ApiModelProperty(value = "设备数量")
    private Integer deviceNum;

    /**
     * 是否为用户当前使用的家庭
     */
    @ApiModelProperty(value = "是否为用户当前使用的家庭")
    private Boolean current;

}
