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

import cc.iotkit.model.rule.RuleInfo;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import io.github.linpeilie.annotations.ReverseAutoMapping;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "rule_info")
@ApiModel(value = "规则")
@AutoMapper(target = RuleInfo.class)
public class TbRuleInfo {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    @ApiModelProperty(value = "规则id")
    private String id;

    @ApiModelProperty(value = "规则名称")
    private String name;

    @ApiModelProperty(value = "规则类型")
    private String type;

    @Column(columnDefinition = "text")
    @ApiModelProperty(value = "监听器")
    @AutoMapping(ignore = true)
    @ReverseAutoMapping(ignore = true)
    private String listeners;

    @Column(columnDefinition = "text")
    @AutoMapping(ignore = true)
    @ReverseAutoMapping(ignore = true)
    @ApiModelProperty(value = "过滤器")
    private String filters;

    @Column(columnDefinition = "text")
    @ApiModelProperty(value = "动作")
    @AutoMapping(ignore = true)
    @ReverseAutoMapping(ignore = true)
    private String actions;

    @ApiModelProperty(value = "用户id")
    private String uid;

    @ApiModelProperty(value = "状态")
    private String state;

    @Column(name = "[desc]")
    @ApiModelProperty(value = "描述")
    private String desc;

    @ApiModelProperty(value = "创建时间")
    private Long createAt;

}
