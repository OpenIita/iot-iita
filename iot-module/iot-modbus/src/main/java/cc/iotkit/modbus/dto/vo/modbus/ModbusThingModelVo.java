package cc.iotkit.modbus.dto.vo.modbus;

import cc.iotkit.model.modbus.ModbusThingModel;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@ApiModel(value = "ModbusThingModelVo")
@Data
@AutoMapper(target = ModbusThingModel.class)
public class ModbusThingModelVo implements Serializable {
    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "模型内容")
    private ModbusThingModel.Model model;

    @ApiModelProperty(value = "产品key")
    private String productKey;

    @ApiModelProperty(value = "更新时间")
    private Long updateAt;
}
