package cc.iotkit.manager.dto.vo.product;

import cc.iotkit.model.product.IconType;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@ApiModel(value = "IconTypeVo")
@Data
@AutoMapper(target = IconType.class)
public class IconTypeVo implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String typeName;

    @ApiModelProperty(value = "分类描述")
    private String typeDescribe;

}
