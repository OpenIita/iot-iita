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

package cc.iotkit.manager.dto.vo.deviceinfo;

import cc.iotkit.model.device.DeviceInfo;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serializable;


@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DeviceInfo.class,convertGenerate = false)
public class DeviceInfoImportVo implements Serializable {
    private static final long serialVersionUID = -1L;

    @ExcelProperty(value = "设备名称")
    private String deviceName;

    @ExcelProperty(value = "设备型号")
    private String model;

    @ExcelProperty(value = "父级id")
    private String parentId;

    @ExcelProperty(value = "产品key")
    private String productKey;

    @ExcelProperty(value = "设备分组")
    private String deviceGroup;
}
