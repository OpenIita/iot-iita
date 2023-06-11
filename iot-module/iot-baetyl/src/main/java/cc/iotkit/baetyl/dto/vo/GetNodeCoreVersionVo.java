package cc.iotkit.baetyl.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: regan
 * @description:
 * @date:created in 2023/6/11 18:46
 * @modificed by:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetNodeCoreVersionVo {
    @ApiModelProperty(value = "当前节点 core 版本号列表")
    private String[] versions;
}
