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
import cc.iotkit.system.dto.bo.SysAppBo;
import cc.iotkit.system.dto.vo.SysAppVo;

import java.util.List;

/**
 * 应用信息Service接口
 *
 * @author tfd
 * @date 2023-08-10
 */
public interface ISysAppService {

    /**
     * 查询应用信息
     */
    SysAppVo queryById(Long id);

    /**
     * 根据appid查询应用信息
     */
    SysAppVo queryByAppId(String appId);

    /**
     * 根据appid查询应用信息是否存在
     */
    boolean checkAppIdUnique(String appId);

    /**
     * 查询应用信息列表
     */
    Paging<SysAppVo> queryPageList(PageRequest<SysAppBo> pageQuery);

    /**
     * 查询应用信息列表
     */
    List<SysAppVo> queryList(SysAppBo bo);

    /**
     * 新增应用信息
     */
    Long insertByBo(SysAppBo bo);

    /**
     * 修改应用信息
     */
    Boolean updateByBo(SysAppBo bo);

    /**
     * 删除应用信息信息
     */
    Boolean deleteById(Long id);
}
