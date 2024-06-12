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

package cc.iotkit.manager.dto.bo.deviceconfig;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.model.device.DeviceConfig;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


@ApiModel(value = "DeviceConfigBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DeviceConfig.class, reverseConvertGenerate = false)
public class DeviceConfigBo extends BaseDto {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "设备配置json内容")
    @Size(max = 65535, message = "设备配置json内容长度不正确")
    private String config;

    @ApiModelProperty(value = "创建时间")
    private Long createAt;

    @ApiModelProperty(value = "设备id")
    @Size(max = 255, message = "设备id长度不正确")
    private String deviceId;

    @ApiModelProperty(value = "设备名称")
    @Size(max = 255, message = "设备名称长度不正确")
    private String deviceName;

    @ApiModelProperty(value = "产品key")
    @Size(max = 255, message = "产品key长度不正确")
    private String productKey;

}
