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

package cc.iotkit.openapi.dto.vo;

import cc.iotkit.model.product.ThingModel;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@ApiModel(value = "OpenPropertyVo")
@Data
public class OpenPropertyVo {
    private String identifier;
    private ThingModel.DataType dataType;
    private String name;
    private String accessMode = "rw";

    // 描述
    private String description;

    // 单位
    private String unit;

    private String time;

    private String value;

    public OpenPropertyVo() {
    }

    public OpenPropertyVo(String identifier, ThingModel.DataType dataType, String name, String accessMode, String description, String unit) {
        this.identifier = identifier;
        this.dataType = dataType;
        this.name = name;
        this.accessMode = accessMode;
        this.description = description;
        this.unit = unit;
    }
}
