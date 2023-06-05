package cc.iotkit.manager.dto.bo.taskinfo;

import cc.iotkit.model.rule.TaskInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.linpeilie.annotations.AutoMapping;
import io.github.linpeilie.annotations.ReverseAutoMapping;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.common.validate.AddGroup;
import cc.iotkit.common.validate.EditGroup;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;


@ApiModel(value = "TaskInfoBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = TaskInfo.class, reverseConvertGenerate = false)
public class TaskInfoBo extends BaseDto {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "任务输出")
    @Size(max = 65535, message = "任务输出长度不正确")
    @AutoMapping(ignore = true)
    @ReverseAutoMapping(ignore = true)
    private String actions;

    @ApiModelProperty(value = "创建时间")
    private Long createAt;

    @ApiModelProperty(value = "描述")
    @Size(max = 255, message = "描述长度不正确")
    private String desc;

    @ApiModelProperty(value = "表达式")
    @Size(max = 255, message = "表达式长度不正确")
    private String expression;

    @ApiModelProperty(value = "任务名称")
    @Size(max = 255, message = "任务名称长度不正确")
    private String name;

    @ApiModelProperty(value = "操作备注")
    @Size(max = 255, message = "操作备注长度不正确")
    private String reason;

    @ApiModelProperty(value = "任务状态")
    @Size(max = 255, message = "任务状态长度不正确")
    private String state;

    @ApiModelProperty(value = "任务类型")
    @Size(max = 255, message = "任务类型长度不正确")
    private String type;

    @ApiModelProperty(value = "创建者")
    @Size(max = 255, message = "创建者长度不正确")
    private String uid;

}
