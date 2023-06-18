package cc.iotkit.baetyl.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: regan
 * @description:
 * @date:created in 2023/6/18 18:08
 * @modificed by:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetNodePropertiesVo {

    @ApiModelProperty(value = "map[string]string, value必须为 string")
    private GetNodePropertiesVoBodyState state;

    @ApiModelProperty(value = "map[string]string, value必须为 string")
    private GetNodePropertiesVoBodyMetadata metadata;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetNodePropertiesVoBodyState {
        @ApiModelProperty(value = "map[string]string, value必须为 string")
        private String report;
        @ApiModelProperty(value = "map[string]string, value必须为 string")
        private String desire;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetNodePropertiesVoBodyMetadata {

        @ApiModelProperty(value = "map[string]string, value必须为 string")
        private String report;
        @ApiModelProperty(value = "map[string]string, value必须为 string")
        private String desire;
    }
}
