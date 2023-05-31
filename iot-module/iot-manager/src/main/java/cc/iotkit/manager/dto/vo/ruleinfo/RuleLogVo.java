package cc.iotkit.manager.dto.vo.ruleinfo;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.model.rule.RuleInfo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(value = "RuleLogBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = RuleInfo.class, reverseConvertGenerate = false)
public class RuleLogVo extends BaseDto  {

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
