package cc.iotkit.system.service.impl;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.satoken.utils.LoginHelper;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.system.ISysRoleData;
import cc.iotkit.data.system.ISysRoleDeptData;
import cc.iotkit.data.system.ISysRoleMenuData;
import cc.iotkit.data.system.ISysUserRoleData;
import cc.iotkit.model.system.SysRole;
import cc.iotkit.system.dto.SysRoleDept;
import cc.iotkit.system.dto.SysUserRole;
import cc.iotkit.system.dto.bo.SysRoleBo;
import cc.iotkit.system.dto.vo.SysRoleVo;
import cc.iotkit.system.service.ISysRoleService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色 业务层处理
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysRoleServiceImpl implements ISysRoleService {

    private final ISysRoleData iSysRoleData;
    private final ISysRoleMenuData iSysRoleMenuData;
    private final ISysUserRoleData iSysUserRoleData;
    private final ISysRoleDeptData iSysRoleDeptData;

    @Override
    public Paging<SysRoleVo> selectPageRoleList(SysRoleBo role, PageRequest<?> query) {
        return new Paging<>();
    }

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    public List<SysRoleVo> selectRoleList(SysRoleBo role) {
        List<SysRole> sysRoles = getSysRoles(role);
        return MapstructUtils.convert(sysRoles, SysRoleVo.class);
    }

    private List<SysRole> getSysRoles(SysRoleBo role) {
        return iSysRoleData.selectRoleList(MapstructUtils.convert(role, SysRole.class));
    }


    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<SysRoleVo> selectRolesByUserId(Long userId) {
        List<SysRole> sysRoles = iSysRoleData.selectRolePermissionByUserId(userId);
        List<SysRole> roles = getSysRoles(new SysRoleBo());
        for (SysRole role : roles) {
            for (SysRole sysRole : sysRoles) {
                if (role.getId().longValue() == sysRole.getId().longValue()) {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return MapstructUtils.convert(roles, SysRoleVo.class);
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> perms = iSysRoleData.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (ObjectUtil.isNotNull(perm)) {
                permsSet.addAll(StringUtils.splitList(perm.getRoleKey().trim()));
            }
        }
        return permsSet;
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<SysRoleVo> selectRoleAll() {
        return this.selectRoleList(new SysRoleBo());
    }

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<Long> selectRoleListByUserId(Long userId) {
        return iSysRoleData.selectRoleListByUserId(userId);
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public SysRoleVo selectRoleById(Long roleId) {
        return iSysRoleData.findById(roleId).to(SysRoleVo.class);
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleNameUnique(SysRoleBo role) {
        return iSysRoleData.checkRoleNameUnique(MapstructUtils.convert(role, SysRole.class));
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleKeyUnique(SysRoleBo role) {
        return iSysRoleData.checkRoleKeyUnique(MapstructUtils.convert(role, SysRole.class));
    }

    /**
     * 校验角色是否允许操作
     *
     * @param roleId 角色ID
     */
    @Override
    public void checkRoleAllowed(Long roleId) {
        if (ObjectUtil.isNotNull(roleId) && LoginHelper.isSuperAdmin(roleId)) {
            throw new BizException("不允许操作超级管理员角色");
        }
    }

    /**
     * 校验角色是否有数据权限
     *
     * @param roleId 角色id
     */
    @Override
    public void checkRoleDataScope(Long roleId) {
        if (ObjectUtil.isNull(roleId)) {
            return;
        }
        if (LoginHelper.isSuperAdmin()) {
            return;
        }
        List<SysRoleVo> roles = this.selectRoleList(new SysRoleBo(roleId));
        if (CollUtil.isEmpty(roles)) {
            throw new BizException("没有权限访问角色数据！");
        }

    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public long countUserRoleByRoleId(Long roleId) {
        return iSysUserRoleData.countUserRoleByRoleId(roleId);
    }

    /**
     * 新增保存角色信息
     *
     * @param bo 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertRole(SysRoleBo bo) {
        SysRole role = iSysRoleData.save(bo.to(SysRole.class));
        bo.setRoleId(role.getId());
        insertRoleMenu(bo);
    }

    /**
     * 修改保存角色信息
     *
     * @param bo 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRole(SysRoleBo bo) {
        SysRole role = iSysRoleData.save(bo.to(SysRole.class));
        if (ObjectUtil.isNull(role)) {
            return 0;
        }
        return 1;
    }

    /**
     * 修改角色状态
     *
     * @param roleId 角色ID
     * @param status 角色状态
     * @return 结果
     */
    @Override
    public void updateRoleStatus(Long roleId, String status) {
        SysRole sysRole = new SysRole();
        sysRole.setId(roleId);
        sysRole.setStatus(status);
        iSysRoleData.updateById(sysRole);

    }

    /**
     * 修改数据权限信息
     *
     * @param bo 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void authDataScope(SysRoleBo bo) {
        // 修改角色信息
        iSysRoleData.updateById(MapstructUtils.convert(bo, SysRole.class));
        // 删除角色与部门关联
        iSysRoleDeptData.delete(bo.getRoleId());
        // 新增角色和部门信息（数据权限）
        insertRoleDept(bo);

    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    private void insertRoleMenu(SysRoleBo role) {
    }

    /**
     * 新增角色部门信息(数据权限)
     *
     * @param role 角色对象
     */
    private void insertRoleDept(SysRoleBo role) {
       /* int rows = 1;
        // 新增角色与部门（数据权限）管理
        List<SysRoleDept> list = new ArrayList();
        for (Long deptId : role.getDeptIds()) {
            SysRoleDept rd = new SysRoleDept();
            rd.setRoleId(role.getRoleId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (list.size() > 0) {
            rows = roleDeptMapper.insertBatch(list) ? list.size() : 0;
        }
        return rows;*/
    }

    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleById(Long roleId) {
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleByIds(Long[] roleIds) {
    }

    /**
     * 取消授权用户角色
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    @Override
    public void deleteAuthUser(SysUserRole userRole) {
    }

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要取消授权的用户数据ID
     * @return 结果
     */
    @Override
    public void deleteAuthUsers(Long roleId, Long[] userIds) {
    }

    /**
     * 批量选择授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要授权的用户数据ID
     * @return 结果
     */
    @Override
    public void insertAuthUsers(Long roleId, Long[] userIds) {
    }

    @Override
    public void cleanOnlineUserByRole(Long roleId) {
    }
}
