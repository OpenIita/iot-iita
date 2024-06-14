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
import cc.iotkit.model.notify.Channel;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * author: 石恒
 * date: 2023-05-11 17:53
 * description:
 **/
@Data
@Entity
@Table(name = "channel")
@AutoMapper(target= Channel.class)
public class TbChannel extends BaseEntity implements TenantAware {
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    @ApiModelProperty(value = "通道id")
    private Long id;

    @ApiModelProperty(value = "通道名称")
    private String code;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "创建时间")
    private Long createAt;

    private Long tenantId;
}
