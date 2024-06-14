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
import cc.iotkit.model.system.SysOss;
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

/**
 * OSS对象存储对象
 *
 * @author Lion Li
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_oss")
@ApiModel(value = "OSS对象存储对象")
@AutoMapper(target = SysOss.class)
public class TbSysOss extends BaseEntity implements TenantAware {

    /**
     * 对象存储主键
     */
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    @ApiModelProperty(value = "对象存储主键")
    private Long id;

    /**
     * 租户编号
     */
    @ApiModelProperty(value = "租户编号")
    private Long tenantId;

    /**
     * 文件名
     */
    @ApiModelProperty(value = "文件名")
    private String fileName;

    /**
     * 原名
     */
    @ApiModelProperty(value = "原名")
    private String originalName;

    /**
     * 文件后缀名
     */
    @ApiModelProperty(value = "文件后缀名")
    private String fileSuffix;

    /**
     * URL地址
     */
    @ApiModelProperty(value = "URL地址")
    private String url;

    /**
     * 服务商
     */
    @ApiModelProperty(value = "服务商")
    private String service;

}
