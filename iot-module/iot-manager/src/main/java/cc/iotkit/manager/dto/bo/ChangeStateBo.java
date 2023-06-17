package cc.iotkit.manager.dto.bo;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.model.protocol.ProtocolComponent;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;


import jakarta.validation.constraints.Size;


@ApiModel(value = "ChangeStateBo")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChangeStateBo extends BaseDto {
    private static final long serialVersionUID = -1L;

    @NotBlank(message = "id不能为空")
    @ApiModelProperty(value = "")
    private String id;


    @NotBlank(message = "state不能为空")
    @ApiModelProperty(value = "运行状态")
    @Size(max = 255, message = "运行状态长度不正确")
    private String state;


}
