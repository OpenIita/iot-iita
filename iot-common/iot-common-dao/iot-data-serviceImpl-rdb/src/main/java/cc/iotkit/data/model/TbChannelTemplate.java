package cc.iotkit.data.model;

import cc.iotkit.model.notify.ChannelTemplate;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 石恒
 * @Date: 2023/5/11 20:59
 * @Description:
 */
@Data
@Entity
@Table(name = "channel_template")
@ApiModel(value = "通道模板")
@AutoMapper(target= ChannelTemplate.class)
public class TbChannelTemplate {
    @Id
    @ApiModelProperty(value = "通道模板id")
    private String id;

    @ApiModelProperty(value = "通道配置id")
    private String channelConfigId;

    @ApiModelProperty(value = "通道模板名称")
    private String title;

    @ApiModelProperty(value = "通道模板内容")
    private String content;

    @ApiModelProperty(value = "创建时间")
    private Long createAt;
}
