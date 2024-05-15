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

package cc.iotkit.manager.dto.vo.plugin;

import cc.iotkit.model.plugin.PluginInfo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.util.Date;

@Data
@AutoMapper(target = PluginInfo.class)
public class PluginInfoVo {

    /**
     * id
     */
    private Long id;

    /**
     * 插件包id
     */
    private String pluginId;

    /**
     * 插件名称
     */
    private String name;

    /**
     * 部署方式
     */
    private String deployType;

    /**
     * 插件包文件名
     */
    private String file;

    /**
     * 插件版本
     */
    private String version;

    /**
     * 插件类型
     */
    private String type;

    /**
     * 设备插件协议类型
     */
    private String protocol;

    /**
     * 状态
     */
    private String state;

    /**
     * 描述
     */
    private String description;

    /**
     * 插件配置项描述
     */
    private String configSchema;

    /**
     * 插件配置信息
     */
    private String config;

    /**
     * 插件脚本
     */
    private String script;

    /**
     * 创建时间
     */
    private Date createTime;

}
