package cc.iotkit.modbus.dto.vo.modbus;

import cc.iotkit.model.modbus.ModbusInfo;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@ApiModel(value = "ModbusInfoVo")
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ModbusInfo.class)
public class ModbusInfoVo implements Serializable  {

	private static final long serialVersionUID = -1L;


	@ApiModelProperty(value="id")
	private Long id;


	@ApiModelProperty(value="产品名称")
    @ExcelProperty(value = "产品名称")
	private String name;


	@ApiModelProperty(value="产品Key")
	@ExcelProperty(value = "产品Key")
	private String productKey;


	@ApiModelProperty(value="说明")
	@ExcelProperty(value = "说明")
	private String remark;

	@ApiModelProperty(value="创建时间")
	@ExcelProperty(value = "创建时间")
	private Long createAt;


	@ApiModelProperty(value="修改时间")
	@ExcelProperty(value = "修改时间")
	private Long updateAt;
}
