package cc.iotkit.data.model.system;

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
public class TbSysDictType extends BaseEntity {

    /**
     * 字典主键
     */
    @Id
    private Long dictId;

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}
