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
import cc.iotkit.model.notify.ChannelConfig;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Author: 石恒
 * @Date: 2023/5/11 20:58
 * @Description:
 */
@Data
@Entity
@Table(name = "channel_config")
@ApiModel(value = "通道配置")
@AutoMapper(target = ChannelConfig.class)
public class TbChannelConfig extends BaseEntity implements TenantAware {
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    @ApiModelProperty(value = "通道配置id")
    private Long id;

    @ApiModelProperty(value = "通道id")
    private Long channelId;

    @ApiModelProperty(value = "通道配置名称")
    private String title;

    @ApiModelProperty(value = "通道配置参数")
    @Column(columnDefinition = "TEXT")
    private String param;

    @ApiModelProperty(value = "创建时间")
    private Long createAt;

    private Long tenantId;
}
