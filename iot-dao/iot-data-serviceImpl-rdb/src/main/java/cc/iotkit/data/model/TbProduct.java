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
import cc.iotkit.model.product.Product;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;

@Data
@Entity
@ApiModel(value = "产品")
@Table(name = "product")
@AutoMapper(target = Product.class)
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@EntityListeners(TenantListener.class)
public class TbProduct implements TenantAware {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    @ApiModelProperty(value = "产品id")
    private Long id;

    @Size(max = 30)
    @Column(name = "tenant_id")
    private String tenantId;

    @ApiModelProperty(value = "产品key")
    private String productKey;

    @ApiModelProperty(value = "产品密钥")
    private String productSecret;

    @ApiModelProperty(value = "产品名称")
    private String name;

    @ApiModelProperty(value = "品类")
    private String category;

    @ApiModelProperty(value = "节点类型")
    private Integer nodeType;

    /**
     * 所属平台用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String uid;

    @ApiModelProperty(value = "图片")
    private String img;

    @ApiModelProperty(value = "产品图标ID")
    private Long iconId;

    @ApiModelProperty(value = "是否透传,true/false")
    private Boolean transparent;

    @ApiModelProperty(value = "是否开启设备定位,true/false")
    private Boolean isOpenLocate;

    @ApiModelProperty(value = "定位更新方式")
    private String locateUpdateType;

    @ApiModelProperty(value = "保活时长（秒）")
    private Long keepAliveTime;

    @ApiModelProperty(value = "创建时间")
    private Long createAt;

}