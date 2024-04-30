package cc.iotkit.manager.dto.vo.product;

import cc.iotkit.model.product.Icon;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@ApiModel(value = "IconVo")
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Icon.class)
public class IconVo implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "id")
    @ExcelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "图标分类id")
    private Long iconTypeId;

    @ApiModelProperty(value = "图标名称")
    private String iconName;

    @ApiModelProperty(value = "视窗缩放")
    private String viewBox;

    @ApiModelProperty(value = "命名空间")
    private String xmlns;

    @ApiModelProperty(value = "版本")
    private String version;

    @ApiModelProperty(value = "图标内容")
    private String iconContent;

}
