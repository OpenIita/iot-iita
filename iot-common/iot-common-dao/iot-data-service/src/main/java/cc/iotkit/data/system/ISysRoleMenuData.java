package cc.iotkit.data.system;

import cc.iotkit.data.ICommonData;
import cc.iotkit.model.system.SysRoleMenu;

/**
 * 操作日志数据接口
 *
 * @author sjg
 */
public interface ISysRoleMenuData extends ICommonData<SysRoleMenu, Long> {
    boolean checkMenuExistRole(Long menuId);
}
