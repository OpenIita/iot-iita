/*
 *
 *  * | Licensed 未经许可不能去掉「OPENIITA」相关版权
 *  * +----------------------------------------------------------------------
 *  * | Author: xw2sy@163.com
 *  * +----------------------------------------------------------------------
 *
 *  Copyright [2024] [OPENIITA]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */

package cc.iotkit.data.system;


import cc.iotkit.data.ICommonData;
import cc.iotkit.model.system.SysDept;

import java.util.List;

/**
 * 部门数据接口
 *
 * @author sjg
 */
public interface ISysDeptData extends ICommonData<SysDept, Long> {




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

    /**
     * 根据ID查询所有子部门数（所有状态）
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    List<SysDept> findByDeptId(Long deptId);


    boolean checkDeptNameUnique(String deptName, Long parentId, Long deptId);

    long selectNormalChildrenDeptById(Long deptId);
}
