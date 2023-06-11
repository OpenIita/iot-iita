package cc.iotkit.baetyl.dto.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author: regan
 * @description:
 * @date:created in 2023/6/11 17:58
 * @modificed by:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNodePropertiesBo {

    @ApiModelProperty(value = "节点名称",required = true)
    private String name;

    @ApiModelProperty(value = "map[string]string, value必须为 string")
    private UpdateNodePropertiesBoBody state;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateNodePropertiesBoBody {
        @ApiModelProperty(value = "map[string]string, value必须为 string")
        private String desire;
    }

}
