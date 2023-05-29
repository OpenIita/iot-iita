package cc.iotkit.data.system;

import cc.iotkit.data.ICommonData;
import cc.iotkit.model.system.SysUser;

/**
 * 操作日志数据接口
 *
 * @author sjg
 */
public interface ISysUserData extends ICommonData<SysUser, Long> {

    /**
     * 按部门统计数量
     *
     * @param deptId 部门id
     * @return 数量
     */
    long countByDeptId(Long deptId);
    
}
