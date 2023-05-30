package cc.iotkit.data.model;

import cc.iotkit.data.model.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 岗位表 sys_post
 *
 * @author Lion Li
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_post")
public class TbSysPost extends BaseEntity {

    /**
     * 岗位序号
     */
    @Id
    @ApiModelProperty(value = "岗位序号")
    private Long postId;

    /**
     * 租户编号
     */
    @ApiModelProperty(value = "租户编号")
    private String tenantId;

    /**
     * 岗位编码
     */
    @ApiModelProperty(value = "岗位编码")
    private String postCode;

    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String postName;

    /**
     * 岗位排序
     */
    @ApiModelProperty(value = "岗位排序")
    private Integer postSort;

    /**
     * 状态（0正常 1停用）
     */
    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
