package cc.iotkit.modbus.dto.bo.modbus;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.model.modbus.ModbusInfo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@ApiModel(value = "ModbusInfoBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ModbusInfo.class, reverseConvertGenerate = false)
public class ModbusInfoBo extends BaseDto  {

	private static final long serialVersionUID = -1L;


	@ApiModelProperty(value="id")
	private Long id;

	@ApiModelProperty(value="产品名称")
	private String name;

	@ApiModelProperty(value="产品Key")
	private String productKey;

	@ApiModelProperty(value="说明")
	private String remark;


    }
