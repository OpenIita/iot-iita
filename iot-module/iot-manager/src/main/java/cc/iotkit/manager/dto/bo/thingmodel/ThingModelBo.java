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

package cc.iotkit.manager.dto.bo.thingmodel;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.model.product.ThingModel;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import io.github.linpeilie.annotations.ReverseAutoMapping;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


@ApiModel(value = "ThingModelBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ThingModel.class, reverseConvertGenerate = false)
public class ThingModelBo extends BaseDto {
    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "模型内容")
    @Size(max = 65535, message = "模型内容长度不正确")
    @AutoMapping(ignore = true)
    @ReverseAutoMapping(ignore = true)
    private String model;

    @ApiModelProperty(value = "产品key")
    @Size(min = 16, max = 16, message = "产品key长度不正确")
    private String productKey;

}
