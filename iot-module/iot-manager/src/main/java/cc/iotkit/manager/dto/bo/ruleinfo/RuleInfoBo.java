package cc.iotkit.manager.dto.bo.ruleinfo;

import cc.iotkit.model.rule.RuleInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import jakarta.validation.constraints.Size;


import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.common.validate.AddGroup;
import cc.iotkit.common.validate.EditGroup;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(value = "RuleInfoBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = RuleInfo.class, reverseConvertGenerate = false)
public class RuleInfoBo extends BaseDto  {

	private static final long serialVersionUID = -1L;

	@ApiModelProperty(value="id")
	private String id;

	@ApiModelProperty(value="动作")
	@Size(max = 65535, message = "动作长度不正确")
    	private String actions;

    	@ApiModelProperty(value="创建时间")
    	private Long createAt;

    	@ApiModelProperty(value="描述")
	@Size(max = 255, message = "描述长度不正确")
    	private String desc;

    	@ApiModelProperty(value="过滤器")
	@Size(max = 65535, message = "过滤器长度不正确")
    	private String filters;

    	@ApiModelProperty(value="监听器")
	@Size(max = 65535, message = "监听器长度不正确")
    	private String listeners;

    	@ApiModelProperty(value="规则名称")
	@Size(max = 255, message = "规则名称长度不正确")
    	private String name;

    	@ApiModelProperty(value="状态")
	@Size(max = 255, message = "状态长度不正确")
    	private String state;

    	@ApiModelProperty(value="规则类型")
	@Size(max = 255, message = "规则类型长度不正确")
    	private String type;

    	@ApiModelProperty(value="用户id")
	@Size(max = 255, message = "用户id长度不正确")
    	private String uid;

    }
