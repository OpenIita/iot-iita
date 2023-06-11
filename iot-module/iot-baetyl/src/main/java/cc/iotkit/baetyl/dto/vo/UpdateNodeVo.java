package cc.iotkit.baetyl.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author: regan
 * @description:
 * @date:created in 2023/6/11 18:35
 * @modificed by:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNodeVo {

    @ApiModelProperty(value = "节点名称")
    private String name;
    @ApiModelProperty(value = "命名空间")
    private String namespace;
    @ApiModelProperty(value = "描述信息")
    private String description;
    @ApiModelProperty(value = "可选官方应用，支持 baetyl-function、baetyl-rule")
    private String[] sysApps;
    @ApiModelProperty(value = "节点是否已连接")
    private Boolean ready;
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    @ApiModelProperty(value = "标签")
    private Object labels;
    @ApiModelProperty(value = "注解")
    private Object annotations;

}
