package cc.iotkit.data.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 石恒
 * @Date: 2023/5/11 20:58
 * @Description:
 */
@Data
@Entity
@Table(name = "channel_config")
@ApiModel(value = "通道配置")
public class TbChannelConfig {
    @Id
    @ApiModelProperty(value = "通道配置id")
    private String id;

    @ApiModelProperty(value = "通道id")
    private String channelId;

    @ApiModelProperty(value = "通道配置名称")
    private String title;

    @ApiModelProperty(value = "通道配置参数")
    private String param;

    @ApiModelProperty(value = "创建时间")
    private Long createAt;
}
