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

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
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

    SysUser selectByPhonenumber(String phonenumber);

    SysUser selectTenantUserByPhonenumber(String phonenumber, Long tenantId);

    SysUser selectTenantUserByEmail(String email, Long tenantId);

    SysUser selectUserByEmail(String email);

    SysUser selectTenantUserByUserName(String username, Long tenantId);

    SysUser selectUserByUserName(String username);


    Paging<SysUser> selectAllocatedList(PageRequest<SysUser> to);

    String selectUserPostGroup(String userName);

    String selectUserRoleGroup(String userName);



    Paging<SysUser> selectUnallocatedList(PageRequest<SysUser> to);

    SysUser findByPhonenumber(String phonenumber);
}
