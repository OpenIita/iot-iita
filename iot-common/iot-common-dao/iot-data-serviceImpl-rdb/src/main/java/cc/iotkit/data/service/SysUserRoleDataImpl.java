package cc.iotkit.data.service;

import cc.iotkit.data.dao.SysUserRoleRepository;
import cc.iotkit.data.system.ISysUserRoleData;
import org.springframework.beans.factory.annotation.Autowired;

import static cc.iotkit.data.model.QTbSysUserRole.tbSysUserRole;

/**
 * @Author：tfd
 * @Date：2023/5/30 16:36
 */
public class SysUserRoleDataImpl implements ISysUserRoleData {
    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;

    @Override
    public int deleteByUserId(Long userId) {
        return sysUserRoleRepository.deleteAllByUserId(userId);
    }

    @Override
    public long countUserRoleByRoleId(Long roleId) {
        return sysUserRoleRepository.count(tbSysUserRole.roleId.eq(roleId));
    }
}
