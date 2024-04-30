package cc.iotkit.manager.dto.bo.product;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.model.product.Icon;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


@ApiModel(value = "IconBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Icon.class, reverseConvertGenerate = false)
public class IconBo extends BaseDto {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "id")
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

    @Size(max = 65535, message = "图标内容长度不正确")
    @ApiModelProperty(value = "图标内容")
    private String iconContent;

}
