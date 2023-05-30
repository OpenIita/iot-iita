package cc.iotkit.manager.dto.vo.thingmodel;

import cc.iotkit.model.product.ThingModel;
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


@ApiModel(value = "ThingModelVo")
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ThingModel.class)

public class ThingModelVo implements Serializable  {

	private static final long serialVersionUID = -1L;

	@ApiModelProperty(value="主键")
    @ExcelProperty(value = "主键")
		private String id;

	@ApiModelProperty(value="模型内容")
    @ExcelProperty(value = "模型内容")
		private String model;

	@ApiModelProperty(value="产品key")
    @ExcelProperty(value = "产品key")
		private String productKey;



}
