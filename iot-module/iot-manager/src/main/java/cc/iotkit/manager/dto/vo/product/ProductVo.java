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
import cc.iotkit.model.product.Product;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@ApiModel(value = "ProductVo")
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Product.class)
public class ProductVo implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "产品id")
    @ExcelProperty(value = "产品id")
    private Long id;

    @ApiModelProperty(value = "产品id")
    @ExcelProperty(value = "产品id")
    private String productKey;

    @ApiModelProperty(value = "产品密钥")
    @ExcelProperty(value = "产品密钥")
    private String productSecret;

    @ApiModelProperty(value = "品类")
    @ExcelProperty(value = "品类")
    private String category;

    @ApiModelProperty(value = "创建时间")
    @ExcelProperty(value = "创建时间")
    private Long createAt;

    @ApiModelProperty(value = "图片")
    @ExcelProperty(value = "图片")
    private String img;

    @ApiModelProperty(value = "产品图标ID")
    @ExcelProperty(value = "图片")
    private Long iconId;

    @ApiModelProperty(value = "产品图标信息")
    private Icon icon;

    @ApiModelProperty(value = "产品名称")
    @ExcelProperty(value = "产品名称")
    private String name;

    @ApiModelProperty(value = "节点类型")
    @ExcelProperty(value = "节点类型")
    private Integer nodeType;

    @ApiModelProperty(value = "是否透传,true/false")
    @ExcelProperty(value = "是否透传,true/false")
    private Boolean transparent;

    @ApiModelProperty(value = "是否开启设备定位,true/false")
    @ExcelProperty(value = "是否开启设备定位,true/false")
    private Boolean isOpenLocate;

    @ApiModelProperty(value = "定位更新方式")
    @ExcelProperty(value = "定位更新方式")
    private String locateUpdateType;

    @ApiModelProperty(value = "用户ID")
    @ExcelProperty(value = "用户ID")
    private String uid;

    @ApiModelProperty(value = "保活时长（秒）")
    @ExcelProperty(value = "保活时长（秒）")
    private Long keepAliveTime;

}
