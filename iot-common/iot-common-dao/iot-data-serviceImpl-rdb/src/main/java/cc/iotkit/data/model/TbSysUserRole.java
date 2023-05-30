package cc.iotkit.data.model;

import cc.iotkit.data.model.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户和角色关联 sys_user_role
 *
 * @author Lion Li
 */

@Data
@Entity
@Table(name = "sys_user_role")
public class TbSysUserRole extends BaseEntity {

    @Id
    private Long id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    private Long roleId;

}
