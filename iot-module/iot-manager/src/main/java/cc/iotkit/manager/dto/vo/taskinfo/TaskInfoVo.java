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

package cc.iotkit.manager.dto.vo.taskinfo;

import cc.iotkit.model.rule.RuleAction;
import cc.iotkit.model.rule.TaskInfo;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@ApiModel(value = "TaskInfoVo")
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = TaskInfo.class)
public class TaskInfoVo implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "主键")
    @ExcelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "任务输出")
    @ExcelProperty(value = "任务输出")
    private List<RuleAction> actions;

    @ApiModelProperty(value = "创建时间")
    @ExcelProperty(value = "创建时间")
    private Long createAt;

    @ApiModelProperty(value = "描述")
    @ExcelProperty(value = "描述")
    private String desc;

    @ApiModelProperty(value = "表达式")
    @ExcelProperty(value = "表达式")
    private String expression;

    @ApiModelProperty(value = "任务名称")
    @ExcelProperty(value = "任务名称")
    private String name;

    @ApiModelProperty(value = "操作备注")
    @ExcelProperty(value = "操作备注")
    private String reason;

    @ApiModelProperty(value = "任务状态")
    @ExcelProperty(value = "任务状态")
    private String state;

    @ApiModelProperty(value = "任务类型")
    @ExcelProperty(value = "任务类型")
    private String type;

    @ApiModelProperty(value = "创建者")
    @ExcelProperty(value = "创建者")
    private String uid;

    public Integer getSeconds() {
        if (TaskInfo.TYPE_DELAY.equals(getType())) {
            return Integer.parseInt(getExpression());
        }
        return null;
    }

}
