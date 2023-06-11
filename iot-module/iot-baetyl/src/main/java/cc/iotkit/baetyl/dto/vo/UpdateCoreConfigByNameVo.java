package cc.iotkit.baetyl.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: regan
 * @description:
 * @date:created in 2023/6/11 18:03
 * @modificed by:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCoreConfigByNameVo {

    @ApiModelProperty(value = "节点名称")
    private String name;

    @ApiModelProperty(value = "body内容")
    private UpdateCoreConfigByNameBoBody body;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateCoreConfigByNameBoBody {
        @ApiModelProperty(value = "core版本号")
        private String version;

        @ApiModelProperty(value = "core上报频率")
        private Integer frequency;

        @ApiModelProperty(value = "core 边缘 API 端口")
        private Integer apiport;
    }



}
