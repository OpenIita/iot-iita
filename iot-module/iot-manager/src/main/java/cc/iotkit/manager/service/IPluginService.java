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

package cc.iotkit.manager.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.manager.dto.bo.plugin.PluginInfoBo;
import cc.iotkit.manager.dto.vo.plugin.PluginInfoVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author sjg
 */
public interface IPluginService {

    /**
     * 上传jar包
     *
     * @param file 文件
     * @param id   插件id
     */
    void upload(MultipartFile file, Long id);

    /**
     * 添加插件
     *
     * @param plugin 插件信息
     */
    void addPlugin(PluginInfoBo plugin);

    /**
     * 修改插件信息
     *
     * @param plugin 插件信息
     */
    void modifyPlugin(PluginInfoBo plugin);

    /**
     * 获取插件信息
     *
     * @param id 插件id
     * @return 插件信息
     */
    PluginInfoVo getPlugin(Long id);

    /**
     * 删除插件
     *
     * @param id 插件id
     */
    void deletePlugin(Long id);

    /**
     * 分页查询
     *
     * @param query 查询条件
     */
    Paging<PluginInfoVo> findPagePluginList(PageRequest<PluginInfoBo> query);

    /**
     * 修改插件状态
     *
     * @param plugin 插件信息
     */
    void changeState(PluginInfoBo plugin);
}
