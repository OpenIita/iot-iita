package cc.iotkit.data.model.system;

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
public class TbSysUserRole {

    @Id
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;

}
