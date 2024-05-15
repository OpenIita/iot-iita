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

package cc.iotkit.manager.dto.vo.deviceconfig;

import cc.iotkit.model.device.DeviceConfig;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@ApiModel(value = "DeviceConfigVo")
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DeviceConfig.class)

public class DeviceConfigVo implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "设备配置id")
    @ExcelProperty(value = "设备配置id")
    private String id;

    @ApiModelProperty(value = "设备配置json内容")
    @ExcelProperty(value = "设备配置json内容")
    private String config;

    @ApiModelProperty(value = "创建时间")
    @ExcelProperty(value = "创建时间")
    private Long createAt;

    @ApiModelProperty(value = "设备id")
    @ExcelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "设备名称")
    @ExcelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "产品key")
    @ExcelProperty(value = "产品key")
    private String productKey;


}
