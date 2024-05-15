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
import cc.iotkit.system.dto.bo.SysTenantPackageBo;
import cc.iotkit.system.dto.vo.SysTenantPackageVo;

import java.util.Collection;
import java.util.List;

/**
 * 租户套餐Service接口
 *
 * @author Michelle.Chung
 */
public interface ISysTenantPackageService {

    /**
     * 查询租户套餐
     */
    SysTenantPackageVo queryById(Long packageId);

    /**
     * 查询租户套餐列表
     */
    Paging<SysTenantPackageVo> queryPageList( PageRequest<SysTenantPackageBo> query);

    /**
     * 查询租户套餐已启用列表
     */
    List<SysTenantPackageVo> selectList();

    /**
     * 查询租户套餐列表
     */
    List<SysTenantPackageVo> queryList(SysTenantPackageBo bo);

    /**
     * 新增租户套餐
     */
    Boolean insertByBo(SysTenantPackageBo bo);

    /**
     * 修改租户套餐
     */
    Boolean updateByBo(SysTenantPackageBo bo);

    /**
     * 修改套餐状态
     */
    void updatePackageStatus(SysTenantPackageBo bo);

    /**
     * 校验并批量删除租户套餐信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
