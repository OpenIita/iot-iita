package cc.iotkit.modbus.dto.bo.modbus;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.model.modbus.ModbusThingModel;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import io.github.linpeilie.annotations.ReverseAutoMapping;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


@ApiModel(value = "ModbusThingModelBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ModbusThingModel.class, reverseConvertGenerate = false)
public class ModbusThingModelBo extends BaseDto {
    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "模型内容")
    @Size(max = 65535, message = "模型内容长度不正确")
    @AutoMapping(ignore = true)
    @ReverseAutoMapping(ignore = true)
    private String model;

    @NotBlank(message="产品Key不能为空")
    @ApiModelProperty(value = "产品key")
    @Size(min = 16, max = 16, message = "产品key长度不正确")
    private String productKey;

}
