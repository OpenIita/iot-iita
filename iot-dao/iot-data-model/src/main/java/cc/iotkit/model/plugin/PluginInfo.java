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

package cc.iotkit.model.plugin;

import cc.iotkit.model.BaseModel;
import cc.iotkit.model.Id;
import cc.iotkit.model.TenantModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 插件信息
 *
 * @author sjg
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PluginInfo  extends TenantModel implements Id<Long>, Serializable {

    /**
     * 插件状态-停止
     */
    public static final String STATE_STOPPED = "stopped";
    /**
     * 插件状态-运行中
     */
    public static final String STATE_RUNNING = "running";

    /**
     * 插件类型-普通插件
     */
    public static final String TYPE_NORMAL = "normal";
    /**
     * 插件类型-设备插件
     */
    public static final String TYPE_DEVICE = "device";

    /**
     * 部署方式-上传jar
     */
    public static final String DEPLOY_UPLOAD = "upload";
    /**
     * 部署方式-独立运行
     */
    public static final String DEPLOY_ALONE = "alone";

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
     * 描述
     */
    private String description;

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
     * 插件配置项描述信息
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

}
