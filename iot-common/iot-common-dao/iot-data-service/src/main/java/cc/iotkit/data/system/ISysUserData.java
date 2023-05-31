package cc.iotkit.data.system;

import cc.iotkit.data.ICommonData;
import cc.iotkit.model.system.SysUser;

/**
 * 用户数据接口
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


    boolean checkUserNameUnique(SysUser to);

    boolean checkPhoneUnique(SysUser to);

    boolean checkEmailUnique(SysUser to);
}
