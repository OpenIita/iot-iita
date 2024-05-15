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
import cc.iotkit.system.dto.bo.SysOperLogBo;
import cc.iotkit.system.dto.vo.SysOperLogVo;

import java.util.Collection;
import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author Lion Li
 */
public interface ISysOperLogService {

    Paging<SysOperLogVo> selectPageOperLogList(PageRequest<?> query);

    /**
     * 新增操作日志
     *
     * @param bo 操作日志对象
     */
    void insertOperlog(SysOperLogBo bo);

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    List<SysOperLogVo> selectOperLogList(SysOperLogBo operLog);

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    void deleteOperLogByIds(Collection<Long> operIds);

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    SysOperLogVo selectOperLogById(Long operId);

    /**
     * 清空操作日志
     */
    void cleanOperLog();
}
