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

package cc.iotkit.manager.dto.bo.product;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.model.product.Product;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;


@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ProductBo.class, reverseConvertGenerate = false)
public class ProductListReqBo extends BaseDto {

    private static final long serialVersionUID = -1L;


    @ApiModelProperty(value = "productKey")
    private String productKey;

    @ApiModelProperty(value = "产品名称")
    @Size(max = 255, message = "产品名称长度不正确")
    private String name;

}
