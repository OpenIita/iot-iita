package cc.iotkit.baetyl.dto.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: regan
 * @description:
 * @date:created in 2023/6/18 16:22
 * @modificed by:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetInstallCommandBo {

    @ApiModelProperty(value = "节点名称")
    private String name;

    @ApiModelProperty(value = "缺省或kube为kube安装命令")
    private String mode;


}
