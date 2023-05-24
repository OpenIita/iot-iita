package cc.iotkit.data.system;


import cc.iotkit.data.ICommonData;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.system.SysDept;

import java.util.List;

/**
 * 部门数据接口
 *
 * @author sjg
 */
public interface ISysDeptData extends ICommonData<SysDept, Long> {

    /**
     * 按条件分页查询
     */
    Paging<SysDept> findByConditions(Long parentId, String deptName,
                                     String status, int page, int size);

    /**
     * 复合备件查询
     *
     * @param dept dept
     * @return list
     */
    List<SysDept> findDepts(SysDept dept);

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    List<SysDept> findByRoleId(Long roleId);

    /**
     * 根据ID查询所有子部门数（正常状态）
     *
     * @param parentId 部门ID
     * @return 子部门数
     */
    long countByParentId(Long parentId);

}
