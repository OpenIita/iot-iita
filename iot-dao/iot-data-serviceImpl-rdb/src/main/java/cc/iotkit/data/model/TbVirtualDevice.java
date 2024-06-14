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
import cc.iotkit.model.device.VirtualDevice;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "virtual_device")
@AutoMapper(target = VirtualDevice.class)
public class TbVirtualDevice extends BaseEntity implements TenantAware {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    private String id;

    /**
     * 所属用户
     */
    @ApiModelProperty(value = "所属用户")
    private String uid;

    /**
     * 虚拟设备名称
     */
    @ApiModelProperty(value = "虚拟设备名称")
    private String name;

    /**
     * 产品key
     */
    @ApiModelProperty(value = "产品key")
    private String productKey;

    /**
     * 虚拟类型
     */
    @ApiModelProperty(value = "虚拟类型")
    private String type;

    /**
     * 设备行为脚本
     */
    @ApiModelProperty(value = "设备行为脚本")
    @Column(columnDefinition = "text")
    private String script;

    /**
     * 触发方式执行方式
     */
    @ApiModelProperty(value = "触发方式执行方式")
    @Column(name = "[trigger]")
    private String trigger;

    /**
     * 触发表达式
     */
    @ApiModelProperty(value = "触发表达式")
    private String triggerExpression;

    /**
     * 运行状态
     */
    @ApiModelProperty(value = "运行状态")
    private String state;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long createAt;

    private Long tenantId;

}
