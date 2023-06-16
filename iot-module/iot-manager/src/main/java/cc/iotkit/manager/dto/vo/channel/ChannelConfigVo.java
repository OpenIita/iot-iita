package cc.iotkit.manager.dto.vo.channel;

import cc.iotkit.model.notify.ChannelConfig;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import io.github.linpeilie.annotations.ReverseAutoMapping;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "ChannelConfigVo")
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ChannelConfig.class)
public class ChannelConfigVo implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value="通道配置id")
    @ExcelProperty(value = "通道配置id")
    private Long id;

    @ApiModelProperty(value="通道id")
    @ExcelProperty(value = "通道id")
    private String channelId;

    @ApiModelProperty(value="通道配置名称")
    @ExcelProperty(value = "通道配置名称")
    private String title;

    @ApiModelProperty(value="通道配置参数")
    @ExcelProperty(value = "通道配置参数")
    @ReverseAutoMapping(target = "param", expression = "java(cc.iotkit.common.utils.JsonUtils.toJsonString(source.getParam()))")
    @AutoMapping(target = "param", expression = "java(cc.iotkit.common.utils.JsonUtils.parse(source.getParam(), ChannelConfig.ChannelParam.class))")
    private String param;

    @ApiModelProperty(value="创建时间")
    @ExcelProperty(value = "创建时间")
    private Long createAt;
}
