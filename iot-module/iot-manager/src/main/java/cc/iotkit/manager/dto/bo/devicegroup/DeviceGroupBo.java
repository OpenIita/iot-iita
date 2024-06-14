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

package cc.iotkit.manager.dto.bo.devicegroup;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.model.device.DeviceGroup;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


@ApiModel(value = "DeviceGroupBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DeviceGroup.class, reverseConvertGenerate = false)
public class DeviceGroupBo extends BaseDto {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "分组id")
    private String id;

    @ApiModelProperty(value = "创建时间")
    private Long createAt;

    @ApiModelProperty(value = "设备数量")
    private Integer deviceQty;

    @ApiModelProperty(value = "设备组名称")
    @Size(max = 255, message = "设备组名称长度不正确")
    private String name;

    @ApiModelProperty(value = "分组说明")
    @Size(max = 255, message = "分组说明长度不正确")
    private String remark;

    @ApiModelProperty(value = "所属用户")
    @Size(max = 255, message = "所属用户长度不正确")
    private String uid;

}
