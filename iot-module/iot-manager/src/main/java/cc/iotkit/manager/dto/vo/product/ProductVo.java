package cc.iotkit.manager.dto.vo.product;

import cc.iotkit.model.product.Product;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;


@ApiModel(value = "ProductVo")
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Product.class)

public class ProductVo implements Serializable  {

	private static final long serialVersionUID = -1L;

	@ApiModelProperty(value="产品id")
    @ExcelProperty(value = "产品id")
		private Long id;

	@ApiModelProperty(value="产品id")
	@ExcelProperty(value = "产品id")
	private String productKey;

	@ApiModelProperty(value="品类")
    @ExcelProperty(value = "品类")
		private String category;

	@ApiModelProperty(value="创建时间")
    @ExcelProperty(value = "创建时间")
		private Long createAt;

	@ApiModelProperty(value="图片")
    @ExcelProperty(value = "图片")
		private String img;

	@ApiModelProperty(value="产品名称")
    @ExcelProperty(value = "产品名称")
		private String name;

	@ApiModelProperty(value="节点类型")
    @ExcelProperty(value = "节点类型")
		private Integer nodeType;

	@ApiModelProperty(value="是否透传,true/false")
    @ExcelProperty(value = "是否透传,true/false")
		private String transparent;

	@ApiModelProperty(value="用户ID")
    @ExcelProperty(value = "用户ID")
		private String uid;



}
