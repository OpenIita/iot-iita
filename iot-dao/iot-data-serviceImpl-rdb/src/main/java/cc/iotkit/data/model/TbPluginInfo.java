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
import cc.iotkit.common.tenant.listener.TenantListener;
import cc.iotkit.model.plugin.PluginInfo;
import cc.iotkit.model.product.Product;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author sjg
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@ApiModel(value = "插件信息")
@Table(name = "plugin_info")
@DynamicUpdate
@AutoMapper(target = PluginInfo.class)
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@EntityListeners(TenantListener.class)
public class TbPluginInfo extends BaseEntity implements TenantAware {

    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 插件包id
     */
    @ApiModelProperty(value = "插件包id")
    private String pluginId;

    /**
     * 插件名称
     */
    @ApiModelProperty(value = "插件名称")
    private String name;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 部署方式
     */
    @ApiModelProperty(value = "部署方式")
    private String deployType;

    /**
     * 插件包地址
     */
    @ApiModelProperty(value = "插件包地址")
    private String file;

    /**
     * 插件版本
     */
    @ApiModelProperty(value = "插件版本")
    private String version;

    /**
     * 插件类型
     */
    @ApiModelProperty(value = "插件类型")
    private String type;

    /**
     * 设备插件协议类型
     */
    @ApiModelProperty(value = "设备插件协议类型")
    private String protocol;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String state;

    /**
     * 插件配置项描述信息
     */
    @ApiModelProperty(value = "插件配置项描述信息")
    @Column(columnDefinition = "text")
    private String configSchema;

    /**
     * 插件配置信息
     */
    @ApiModelProperty(value = "插件配置信息")
    @Column(columnDefinition = "text")
    private String config;

    /**
     * 插件脚本
     */
    @ApiModelProperty(value = "插件脚本")
    @Column(columnDefinition = "text")
    private String script;

    /**
     * 租户编号
     */
    @ApiModelProperty(value = "租户编号")
    private String tenantId;
}
