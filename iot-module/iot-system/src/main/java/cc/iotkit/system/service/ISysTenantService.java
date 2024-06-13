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

package cc.iotkit.system.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.system.dto.bo.SysTenantBo;
import cc.iotkit.system.dto.vo.SysTenantVo;

import java.util.List;

/**
 * 租户Service接口
 *
 * @author Michelle.Chung
 */
public interface ISysTenantService {

    /**
     * 查询租户
     */
    SysTenantVo queryById(Long id);

    /**
     * 基于租户ID查询租户
     */
    SysTenantVo queryByTenantId(Long tenantId);

    /**
     * 查询租户列表
     */
    Paging<SysTenantVo> queryPageList(  PageRequest<SysTenantBo> query);

    /**
     * 查询租户列表
     */
    List<SysTenantVo> queryList(SysTenantBo bo);

    /**
     * 新增租户
     */
    void insertByBo(SysTenantBo bo);

    /**
     * 修改租户
     */
    void updateByBo(SysTenantBo bo);

    /**
     * 修改租户状态
     */
    int updateTenantStatus(SysTenantBo bo);

    /**
     * 校验租户是否允许操作
     */
    void checkTenantAllowed(Long tenantId);

    /**
     * 删除租户信息
     */
    void deleteById(Long id);

    /**
     * 校验企业名称是否唯一
     */
    boolean checkCompanyNameUnique(SysTenantBo bo);

    /**
     * 校验账号余额
     */
    boolean checkAccountBalance(Long tenantId);

    /**
     * 校验有效期
     */
    boolean checkExpireTime(Long tenantId);

    /**
     * 同步租户套餐
     */
    Boolean syncTenantPackage(Long tenantId, String packageId);
}
