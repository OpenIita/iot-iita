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
import lombok.*;

import java.io.Serializable;

/**
 * 插件实例
 *
 * @author sjg
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PluginInstance  extends TenantModel implements Id<Long>, Serializable {

    private Long id;

    /**
     * 插件主程序id
     */
    private String mainId;

    /**
     * 插件id
     */
    private Long pluginId;

    /**
     * 插件主程序所在ip
     */
    private String ip;

    /**
     * 插件主程序端口
     */
    private int port;

    /**
     * 心跳时间
     * 心路时间超过30秒需要剔除
     */
    private Long heartbeatAt;

}
