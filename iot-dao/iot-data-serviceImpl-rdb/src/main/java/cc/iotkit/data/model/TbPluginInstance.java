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

package cc.iotkit.data.model;

import cc.iotkit.common.tenant.dao.TenantAware;
import cc.iotkit.common.tenant.entiry.BaseTenantEntity;
import cc.iotkit.model.plugin.PluginInstance;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@ApiModel(value = "插件实例")
@Table(name = "plugin_instance")
@AutoMapper(target = PluginInstance.class)
public class TbPluginInstance extends BaseEntity implements TenantAware {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 插件主程序id
     */
    @ApiModelProperty(value = "插件主程序id")
    private String mainId;

    /**
     * 插件id
     */
    @ApiModelProperty(value = "插件id")
    private Long pluginId;

    /**
     * 插件主程序所在ip
     */
    @ApiModelProperty(value = "插件主程序所在ip")
    private String ip;

    /**
     * 插件主程序端口
     */
    @ApiModelProperty(value = "插件主程序端口")
    private int port;

    /**
     * 心跳时间
     * 心路时间超过30秒需要剔除
     */
    @ApiModelProperty(value = "心跳时间")
    private Long heartbeatAt;

    private Long tenantId;

}
