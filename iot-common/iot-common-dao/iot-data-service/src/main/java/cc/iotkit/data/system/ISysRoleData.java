package cc.iotkit.data.system;

import cc.iotkit.data.ICommonData;
import cc.iotkit.model.system.SysRole;

import java.util.List;

/**
 * 操作日志数据接口
 *
 * @author sjg
 */
public interface ISysRoleData extends ICommonData<SysRole, Long> {

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    List<Long> selectMenuListByRoleId(Long roleId, boolean menuCheckStrictly);
}
