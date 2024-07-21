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
import cc.iotkit.model.system.SysApp;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;

/**
 * 应用信息表对象 SYS_APP
 *
 * @author tfd
 * @date 2023-08-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Table(name = "SYS_APP")
@AutoMapper(target = SysApp.class)
@ApiModel(value = "应用信息表")
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "long")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@EntityListeners(TenantListener.class)
public class TbSysApp extends BaseEntity implements TenantAware {

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 租户编号
     */
    @ApiModelProperty(value = "租户编号")
    private Long tenantId;

    /**
     * 应用名称
     */
    @ApiModelProperty(value = "应用名称")
    private String appName;

    /**
     * appId
     */
    @ApiModelProperty(value = "appId")
    private String appId;

    /**
     * appSecret
     */
    @ApiModelProperty(value = "appSecret")
    private String appSecret;

    /**
     * 应用类型,0:app,1:小程序
     */
    @ApiModelProperty(value = "应用类型")
    private String appType;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;


}
