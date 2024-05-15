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

package cc.iotkit.data.model;

import cc.iotkit.model.product.ProductModel;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@ApiModel(value = "产品型号")
@Table(name = "product_model")
@AutoMapper(target = ProductModel.class)
public class TbProductModel {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    @ApiModelProperty(value = "型号id")
    private String id;

    /**
     * 型号在所有产品中唯一
     */
    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "名称")

    private String name;

    @ApiModelProperty(value = "产品Key")

    private String productKey;

    @ApiModelProperty(value = "脚本类型")
    private String type;

    @Column(columnDefinition = "text")
    @ApiModelProperty(value = "脚本内容")

    private String script;

    /**
     * 脚本状态，只有发布状态才生效
     */
    @ApiModelProperty(value = "脚本状态")

    private String state;
    @ApiModelProperty(value = "修改时间")
    private Long modifyAt;
}
