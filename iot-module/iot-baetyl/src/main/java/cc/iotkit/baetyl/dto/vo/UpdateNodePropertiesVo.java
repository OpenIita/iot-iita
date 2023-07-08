package cc.iotkit.baetyl.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: regan
 * @description:
 * @date:created in 2023/6/11 17:58
 * @modificed by:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNodePropertiesVo {

    @ApiModelProperty(value = "map[string]string, value必须为 string")
    private UpdateNodePropertiesVoBodyState state;

    @ApiModelProperty(value = "map[string]string, value必须为 string")
    private UpdateNodePropertiesVoBodyMetadata metadata;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateNodePropertiesVoBodyState {
        @ApiModelProperty(value = "map[string]string, value必须为 string")
        private String report;
        @ApiModelProperty(value = "map[string]string, value必须为 string")
        private String desire;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateNodePropertiesVoBodyMetadata {

        @ApiModelProperty(value = "map[string]string, value必须为 string")
        private String report;
        @ApiModelProperty(value = "map[string]string, value必须为 string")
        private String desire;
    }
}
