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
import cc.iotkit.model.rule.RuleLog;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(value = "RuleLogBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = RuleLog.class, reverseConvertGenerate = false)
public class RuleLogBo extends BaseDto  {

	private static final long serialVersionUID = -1L;

	@ApiModelProperty(value="时间")
	private Long time;

	@ApiModelProperty(value="规则id")
	private String ruleId;

	@ApiModelProperty(value="状态")
	private String state1;

	@ApiModelProperty(value="内容")
	private String content;

	@ApiModelProperty(value="是否成功")
	private Boolean success;

    }
