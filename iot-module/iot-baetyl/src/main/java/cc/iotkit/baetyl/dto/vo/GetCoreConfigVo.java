package cc.iotkit.baetyl.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: regan
 * @description:
 * @date:created in 2023/6/18 15:58
 * @modificed by:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCoreConfigVo {

    @ApiModelProperty(value = "core 版本")
    private String version;

    @ApiModelProperty(value = "core 上报频率")
    private Integer frequency;

    @ApiModelProperty(value = "core 边缘 API 端口")
    private Integer apiport;
}
