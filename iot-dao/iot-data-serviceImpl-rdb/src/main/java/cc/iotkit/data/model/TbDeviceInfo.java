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
import cc.iotkit.model.device.DeviceInfo;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import io.github.linpeilie.annotations.ReverseAutoMapping;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;

@Data
@Entity
@Table(name = "device_info")
@ApiModel(value = "设备信息")
@AutoMapper(target = DeviceInfo.class)
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "long")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@EntityListeners(TenantListener.class)
public class TbDeviceInfo extends BaseEntity implements TenantAware {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    private String id;

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "产品key")
    private String productKey;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "设备类型")
    private String model;

    @ApiModelProperty(value = "设备密钥")
    private String secret;

    @ApiModelProperty(value = "父级id")
    private String parentId;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "用户id")
    private String uid;

    @ApiModelProperty(value = "设备状态")
    @AutoMapping(ignore = true)
    @ReverseAutoMapping(ignore = true)
    private String state;

    @ApiModelProperty(value = "设备在线时间")
    private Long onlineTime;

    @ApiModelProperty(value = "设备离线时间")
    private Long offlineTime;

    @ApiModelProperty(value = "创建时间")
    private Long createAt;

    /**
     * 租户编号
     */
    @ApiModelProperty(value = "租户编号")
    private Long tenantId;

}
