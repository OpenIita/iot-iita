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

package cc.iotkit.manager.dto.bo.plugin;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.common.validate.AddGroup;
import cc.iotkit.common.validate.EditGroup;
import cc.iotkit.model.plugin.PluginInfo;
import io.github.linpeilie.annotations.AutoMapper;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 插件业务对象
 *
 * @author sjg
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = PluginInfo.class, reverseConvertGenerate = false)
public class PluginInfoBo extends BaseDto {

    /**
     * id
     */
    @NotNull(message = "插件id不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 插件名称
     */
    @NotNull(message = "插件名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

    /**
     * 部署方式
     */
    @NotNull(message = "部署方式不能为空", groups = {AddGroup.class, EditGroup.class})
    private String deployType;

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
     * 插件配置信息
     */
    private String config;

    /**
     * 插件脚本
     */
    private String script;

}
