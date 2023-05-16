package cc.iotkit.system.domain;

import lombok.Data;

/**
 * 角色和部门关联
 *
 * @author Lion Li
 */

@Data
public class SysRoleDept {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 部门ID
     */
    private Long deptId;

}
