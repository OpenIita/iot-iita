package cc.iotkit.manager.dto.vo.category;

import cc.iotkit.model.product.Category;
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


@ApiModel(value = "CategoryVo")
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Category.class)

public class CategoryVo implements Serializable  {

	private static final long serialVersionUID = -1L;

	@ApiModelProperty(value="分类id")
    @ExcelProperty(value = "分类id")
		private String id;

	@ApiModelProperty(value="分类描述")
    @ExcelProperty(value = "分类描述")
		private Long createAt;

	@ApiModelProperty(value="分类名称")
    @ExcelProperty(value = "分类名称")
		private String name;



}
