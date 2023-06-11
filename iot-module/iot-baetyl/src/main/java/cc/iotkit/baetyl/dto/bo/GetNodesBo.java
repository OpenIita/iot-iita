package cc.iotkit.baetyl.dto.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: regan
 * @description:
 * @date:created in 2023/6/11 18:49
 * @modificed by:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetNodesBo {
    @ApiModelProperty(value = "标签查询")
    private String selector;
    @ApiModelProperty(value = "属性查询")
    private String fieldSelector;
    @ApiModelProperty(value = "分页限制")
    private Integer limit;
    @ApiModelProperty(value = "分页继续token，由上一次分页查询返回")
    private String isContinue;
    @ApiModelProperty(value = "查询页码")
    private Integer pageNo;
    @ApiModelProperty(value = "每页数据数")
    private Integer pageSize;
    @ApiModelProperty(value = "模糊匹配名称")
    private String name;
    @ApiModelProperty(value = "子节点标签过滤")
    private String nodeSelector;
}
