package cc.iotkit.data.model;

import cc.iotkit.data.model.BaseEntity;
import cc.iotkit.model.system.SysDictData;
import cc.iotkit.model.system.SysDictType;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 字典类型表 sys_dict_type
 *
 * @author Lion Li
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_dict_type")
@AutoMapper(target = SysDictType.class)
@ApiModel(value = "字典类型表")
public class TbSysDictType extends BaseEntity {

    /**
     * 字典主键
     */
    @Id
    @ApiModelProperty(value = "字典主键")
    private Long dictId;

    /**
     * 租户编号
     */
    @ApiModelProperty(value = "租户编号")
    private String tenantId;

    /**
     * 字典名称
     */
    @ApiModelProperty(value = "字典名称")
    private String dictName;

    /**
     * 字典类型
     */
    @ApiModelProperty(value = "字典类型")
    private String dictType;

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
