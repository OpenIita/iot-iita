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

package cc.iotkit.manager.dto.bo.productmodel;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.model.product.ProductModel;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


@ApiModel(value = "ProductModelBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ProductModel.class, reverseConvertGenerate = false)
public class ProductModelBo extends BaseDto  {

	private static final long serialVersionUID = -1L;

	@ApiModelProperty(value="id")
	private String id;

	@ApiModelProperty(value="型号")
	@Size(max = 255, message = "型号长度不正确")
    	private String model;

    	@ApiModelProperty(value="修改时间")
    	private Long modifyAt;

    	@ApiModelProperty(value="名称")
	@Size(max = 255, message = "名称长度不正确")
    	private String name;

    	@ApiModelProperty(value="产品Key")
	@Size(max = 255, message = "产品Key长度不正确")
    	private String productKey;

    	@ApiModelProperty(value="脚本内容")
	@Size(max = 65535, message = "脚本内容长度不正确")
    	private String script;

    	@ApiModelProperty(value="脚本状态")
	@Size(max = 255, message = "脚本状态长度不正确")
    	private String state;

    	@ApiModelProperty(value="脚本类型")
	@Size(max = 255, message = "脚本类型长度不正确")
    	private String type;

    }
