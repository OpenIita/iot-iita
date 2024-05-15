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

package cc.iotkit.manager.dto.vo.ota;

import cc.iotkit.model.ota.DeviceOtaDetail;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 石恒
 * @Date: 2023/6/17 20:49
 * @Description:
 */
@Data
@ExcelIgnoreUnannotated
@ApiModel(value = "DeviceOtaDetailVo")
@AutoMapper(target = DeviceOtaDetail.class)
public class DeviceOtaDetailVo implements Serializable {
    private Long id;

    private Integer step;

    private String taskId;

    private String desc;

    private String version;

    private String module;

    private String deviceId;

    private String productKey;

    private String deviceName;

    private Long otaInfoId;
}
