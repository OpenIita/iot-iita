package cc.iotkit.manager.dto.bo.product;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.model.product.Product;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


@ApiModel(value = "ProductBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Product.class, reverseConvertGenerate = false)
public class ProductBo extends BaseDto  {

	private static final long serialVersionUID = -1L;

	@ApiModelProperty(value="id")
	private Long id;
	@ApiModelProperty(value="productKey")
	private String productKey;

	@ApiModelProperty(value="品类")
	@Size(max = 255, message = "品类长度不正确")
    	private String category;

    	@ApiModelProperty(value="创建时间")
    	private Long createAt;

    	@ApiModelProperty(value="图片")
	@Size(max = 255, message = "图片长度不正确")
    	private String img;

    	@ApiModelProperty(value="产品名称")
	@Size(max = 255, message = "产品名称长度不正确")
    	private String name;

    	@ApiModelProperty(value="节点类型")
    	private Integer nodeType;

    	@ApiModelProperty(value="是否透传,true/false")
	@Size(max = 255, message = "是否透传,true/false长度不正确")
    	private String transparent;

    	@ApiModelProperty(value="用户ID")
	@Size(max = 255, message = "用户ID长度不正确")
    	private String uid;

    }
