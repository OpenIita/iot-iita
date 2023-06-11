package cc.iotkit.baetyl.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: regan
 * @description:
 * @date:created in 2023/6/8 16:36
 * @modificed by:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetNodeAppsByNameVo {

    @ApiModelProperty(value = "应用总数")
    private Integer total;
    @ApiModelProperty(value = "列表选项")
    private Object listOptions;

    @ApiModelProperty(value = "应用列表")
    private List<AppInfoDetails> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AppInfoDetails{
        @ApiModelProperty(value = "应用名称",required = true)
        private String name;
        @ApiModelProperty(value = "标签选择器",required = true)
        private String selector;
        @ApiModelProperty(value = "命名空间",required = true)
        private String namespace;
        @ApiModelProperty(value = "创建时间",required = true)
        private String createTime;
    }


}
