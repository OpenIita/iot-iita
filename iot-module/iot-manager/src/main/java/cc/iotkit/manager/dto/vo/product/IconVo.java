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

package cc.iotkit.manager.dto.vo.product;

import cc.iotkit.model.product.Icon;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@ApiModel(value = "IconVo")
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Icon.class)
public class IconVo implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "id")
    @ExcelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "图标分类id")
    private Long iconTypeId;

    @ApiModelProperty(value = "图标名称")
    private String iconName;

    @ApiModelProperty(value = "视窗缩放")
    private String viewBox;

    @ApiModelProperty(value = "命名空间")
    private String xmlns;

    @ApiModelProperty(value = "版本")
    private String version;

    @ApiModelProperty(value = "图标内容")
    private String iconContent;

}
