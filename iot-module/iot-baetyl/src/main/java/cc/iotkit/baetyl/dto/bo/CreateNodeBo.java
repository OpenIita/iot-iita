package cc.iotkit.baetyl.dto.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author: regan
 * @description:
 * @date:created in 2023/6/8 16:36
 * @modificed by:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNodeBo {

    @ApiModelProperty(value = "节点名称",required = true)
    private String name;
    @ApiModelProperty(value = "map[string]string类型，例如 {\"env\":\"test\"}")
    private Map<String,String> labels;
    @ApiModelProperty(value = "map[string]string 用来保存品牌等属性信息")
    private Map<String,String> annotations;
    @ApiModelProperty(value = "为空或NVIDIA GPU")
    private String accelerator;
    @ApiModelProperty(value = "是单机还是集群环境")
    private Boolean cluster;
    @ApiModelProperty(value = "可选官方应用，支持 baetyl-function、baetyl-rule")
    private String[] sysApps;
    @ApiModelProperty(value = "描述信息")
    private String description;

}
