package cc.iotkit.manager.dto.bo.product;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.model.product.IconType;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@ApiModel(value = "IconTypeBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = IconType.class, reverseConvertGenerate = false)
public class IconTypeBo extends BaseDto {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String typeName;

    @ApiModelProperty(value = "分类描述")
    private String typeDescribe;

}
