package cc.iotkit.system.controller;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.excel.utils.ExcelUtil;
import cc.iotkit.common.log.annotation.Log;
import cc.iotkit.common.log.enums.BusinessType;
import cc.iotkit.common.web.core.BaseController;
import cc.iotkit.system.dto.SysUserRole;
import cc.iotkit.system.dto.bo.SysDeptBo;
import cc.iotkit.system.dto.bo.SysRoleBo;
import cc.iotkit.system.dto.bo.SysUserBo;
import cc.iotkit.system.dto.vo.DeptTreeSelectVo;
import cc.iotkit.system.dto.vo.SysRoleVo;
import cc.iotkit.system.dto.vo.SysUserVo;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cc.iotkit.system.service.ISysDeptService;
import cc.iotkit.system.service.ISysRoleService;
import cc.iotkit.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 角色信息
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {

    private final ISysRoleService roleService;
    private final ISysUserService userService;
    private final ISysDeptService deptService;

    /**
     * 获取角色信息列表
     */
    @SaCheckPermission("system:role:list")
    @GetMapping("/list")
    public Paging<SysRoleVo> list(SysRoleBo role, PageRequest<?> query) {
        return roleService.selectPageRoleList(role, query);
    }

    /**
     * 导出角色信息列表
     */
    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("system:role:export")
    @PostMapping("/export")
    public void export(SysRoleBo role, HttpServletResponse response) {
        List<SysRoleVo> list = roleService.selectRoleList(role);
        ExcelUtil.exportExcel(list, "角色数据", SysRoleVo.class, response);
    }

    /**
     * 根据角色编号获取详细信息
     *
     * @param roleId 角色ID
     */
    @SaCheckPermission("system:role:query")
    @GetMapping(value = "/{roleId}")
    public SysRoleVo getInfo(@PathVariable Long roleId) {
        roleService.checkRoleDataScope(roleId);
        return roleService.selectRoleById(roleId);
    }

    /**
     * 新增角色
     */
    @SaCheckPermission("system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public void add(@Validated @RequestBody SysRoleBo role) {
        if (!roleService.checkRoleNameUnique(role)) {
            fail("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            fail("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        roleService.insertRole(role);

    }

    /**
     * 修改保存角色
     */
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public void edit(@Validated @RequestBody SysRoleBo role) {
        roleService.checkRoleAllowed(role.getRoleId());
        roleService.checkRoleDataScope(role.getRoleId());
        if (!roleService.checkRoleNameUnique(role)) {
            fail("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            fail("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }

        if (roleService.updateRole(role) > 0) {
            roleService.cleanOnlineUserByRole(role.getRoleId());
        }
        fail("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
    }

    /**
     * 修改保存数据权限
     */
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    public void dataScope(@RequestBody SysRoleBo role) {
        roleService.checkRoleAllowed(role.getRoleId());
        roleService.checkRoleDataScope(role.getRoleId());
        roleService.authDataScope(role);
    }

    /**
     * 状态修改
     */
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public void changeStatus(@RequestBody SysRoleBo role) {
        roleService.checkRoleAllowed(role.getRoleId());
        roleService.checkRoleDataScope(role.getRoleId());
        roleService.updateRoleStatus(role.getRoleId(), role.getStatus());
    }

    /**
     * 删除角色
     *
     * @param roleIds 角色ID串
     */
    @SaCheckPermission("system:role:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public void remove(@PathVariable Long[] roleIds) {
        roleService.deleteRoleByIds(roleIds);
    }

    /**
     * 获取角色选择框列表
     */
    @SaCheckPermission("system:role:query")
    @GetMapping("/optionselect")
    public List<SysRoleVo> optionselect() {
        return roleService.selectRoleAll();
    }

    /**
     * 查询已分配用户角色列表
     */
    @SaCheckPermission("system:role:list")
    @GetMapping("/authUser/allocatedList")
    public Paging<SysUserVo> allocatedList(SysUserBo user, PageRequest<?> query) {
        return userService.selectAllocatedList(user, query);
    }

    /**
     * 查询未分配用户角色列表
     */
    @SaCheckPermission("system:role:list")
    @GetMapping("/authUser/unallocatedList")
    public Paging<SysUserVo> unallocatedList(SysUserBo user, PageRequest<?> query) {
        return userService.selectUnallocatedList(user, query);
    }

    /**
     * 取消授权用户
     */
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    public void cancelAuthUser(@RequestBody SysUserRole userRole) {
        roleService.deleteAuthUser(userRole);
    }

    /**
     * 批量取消授权用户
     *
     * @param roleId  角色ID
     * @param userIds 用户ID串
     */
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    public void cancelAuthUserAll(Long roleId, Long[] userIds) {
        roleService.deleteAuthUsers(roleId, userIds);
    }

    /**
     * 批量选择用户授权
     *
     * @param roleId  角色ID
     * @param userIds 用户ID串
     */
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    public void selectAuthUserAll(Long roleId, Long[] userIds) {
        roleService.checkRoleDataScope(roleId);
        roleService.insertAuthUsers(roleId, userIds);
    }

    /**
     * 获取对应角色部门树列表
     *
     * @param roleId 角色ID
     */
    @SaCheckPermission("system:role:list")
    @GetMapping(value = "/deptTree/{roleId}")
    public DeptTreeSelectVo roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
        DeptTreeSelectVo selectVo = new DeptTreeSelectVo();
        selectVo.setCheckedKeys(deptService.selectDeptListByRoleId(roleId));
        selectVo.setDepts(deptService.selectDeptTreeList(new SysDeptBo()));
        return selectVo;
    }
}
