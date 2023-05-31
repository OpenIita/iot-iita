package cc.iotkit.manager.dto.bo.devicegroup;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.model.device.DeviceGroup;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@ApiModel(value = "DeviceAddGroupBo")
@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceAddGroupBo extends BaseDto {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty("组id")
    private String group;
    @ApiModelProperty("设备列表")
    private  List<String> devices;

}
