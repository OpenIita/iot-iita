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

package cc.iotkit.system.dto.bo;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.common.validate.AddGroup;
import cc.iotkit.common.validate.EditGroup;
import cc.iotkit.model.system.SysApp;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用信息业务对象 SYS_APP
 *
 * @author tfd
 * @date 2023-08-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysApp.class, reverseConvertGenerate = false)
public class SysAppBo extends BaseDto {


    @NotBlank(message = "id不能为空", groups = { AddGroup.class, EditGroup.class })
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 应用名称
     */
    @ApiModelProperty(value = "应用名称", required = true)
    private String appName;

    /**
     * appId
     */
    @NotBlank(message = "appId不能为空", groups = { AddGroup.class, EditGroup.class })
    @ApiModelProperty(value = "appId", required = true)
    private String appId;

    /**
     * appSecret
     */
    @NotBlank(message = "appSecret不能为空", groups = { AddGroup.class, EditGroup.class })
    @ApiModelProperty(value = "appSecret", required = true)
    private String appSecret;

    /**
     * 应用类型
     */
    @NotBlank(message = "应用类型不能为空", groups = { AddGroup.class, EditGroup.class })
    @ApiModelProperty(value = "应用类型", required = true)
    private String appType;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", required = true)
    private String remark;


}
