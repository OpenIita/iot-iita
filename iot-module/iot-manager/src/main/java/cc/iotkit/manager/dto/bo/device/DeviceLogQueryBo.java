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
package cc.iotkit.manager.dto.bo.device;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.manager.model.vo.DeviceLog;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(value = "DeviceLogQueryBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DeviceLog.class, reverseConvertGenerate = false)
public class DeviceLogQueryBo extends BaseDto {

    @ApiModelProperty(value="设备id")
    private String deviceId;

    @ApiModelProperty(value="类型")
    private String type;

    @ApiModelProperty(value="属性名")
    private String identifier;
}
