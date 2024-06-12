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

package cc.iotkit.manager.dto.bo.ruleinfo;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.model.rule.FilterConfig;
import cc.iotkit.model.rule.RuleAction;
import cc.iotkit.model.rule.RuleInfo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@ApiModel(value = "RuleInfoBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = RuleInfo.class, reverseConvertGenerate = false)
public class RuleInfoBo extends BaseDto {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "动作")
    private List<RuleAction> actions;

    @ApiModelProperty(value = "创建时间")
    private Long createAt;

    @ApiModelProperty(value = "描述")
    @Size(max = 255, message = "描述长度不正确")
    private String desc;

    @ApiModelProperty(value = "过滤器")
    private List<FilterConfig> filters;

    @ApiModelProperty(value = "监听器")
    private List<FilterConfig> listeners;

    @ApiModelProperty(value = "规则名称")
    @Size(max = 255, message = "规则名称长度不正确")
    private String name;

    @ApiModelProperty(value = "状态")
    @Size(max = 255, message = "状态长度不正确")
    private String state;

    @ApiModelProperty(value = "规则类型")
    @Size(max = 255, message = "规则类型长度不正确")
    private String type;

    @ApiModelProperty(value = "用户id")
    @Size(max = 255, message = "用户id长度不正确")
    private String uid;

}
