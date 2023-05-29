package cc.iotkit.data.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色和菜单关联 sys_role_menu
 *
 * @author Lion Li
 */

@Data
@Entity
@Table(name = "sys_role_menu")
public class TbSysRoleMenu {

    @Id
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

}
