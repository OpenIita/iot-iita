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

package cc.iotkit.manager.dto.vo.category;

import cc.iotkit.model.product.Category;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@ApiModel(value = "CategoryVo")
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Category.class)

public class CategoryVo implements Serializable  {

	private static final long serialVersionUID = -1L;

	@ApiModelProperty(value="分类id")
    @ExcelProperty(value = "分类id")
		private String id;

	@ApiModelProperty(value="分类描述")
    @ExcelProperty(value = "分类描述")
		private Long createAt;

	@ApiModelProperty(value="分类名称")
    @ExcelProperty(value = "分类名称")
		private String name;



}
