package cc.iotkit.manager.dto.bo.devicegroup;

import cc.iotkit.model.device.DeviceGroup;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.common.validate.AddGroup;
import cc.iotkit.common.validate.EditGroup;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;


@ApiModel(value = "DeviceGroupBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DeviceGroup.class, reverseConvertGenerate = false)
public class DeviceGroupBo extends BaseDto {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "创建时间")
    @NotNull(message = "创建时间不能为空")
    private Long createAt;

    @ApiModelProperty(value = "设备数量")
    @NotNull(message = "设备数量不能为空")
    private Integer deviceQty;

    @ApiModelProperty(value = "设备组名称")
    @Size(max = 255, message = "设备组名称长度不正确")
    private String name;

    @ApiModelProperty(value = "分组说明")
    @Size(max = 255, message = "分组说明长度不正确")
    private String remark;

    @ApiModelProperty(value = "所属用户")
    @Size(max = 255, message = "所属用户长度不正确")
    private String uid;

}
