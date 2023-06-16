package cc.iotkit.manager.dto.bo.channel;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.model.notify.ChannelConfig;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@ApiModel(value = "ChannelConfigBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ChannelConfig.class, reverseConvertGenerate = false)
public class ChannelConfigBo extends BaseDto  {

	private static final long serialVersionUID = -1L;

	@ApiModelProperty(value="id")
	private String id;

	@ApiModelProperty(value="通道id")
	private Long channelId;

	@ApiModelProperty(value="通道配置名称")
	private String title;

	@ApiModelProperty(value="通道配置参数")
	@AutoMapping(target = "param", expression = "java(cc.iotkit.common.utils.JsonUtils.parse(source.getParam(), ChannelConfig.ChannelParam.class))")
	private String param;

	@ApiModelProperty(value="创建时间")
	private Long createAt;

    }
