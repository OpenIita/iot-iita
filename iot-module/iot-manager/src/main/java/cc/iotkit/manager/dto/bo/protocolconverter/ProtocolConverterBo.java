package cc.iotkit.manager.dto.bo.protocolconverter;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.common.validate.AddGroup;
import cc.iotkit.common.validate.EditGroup;
import cc.iotkit.model.protocol.ProtocolConverter;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


@ApiModel(value = "ProtocolConverterBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ProtocolConverter.class, reverseConvertGenerate = false)
public class ProtocolConverterBo extends BaseDto {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "id")
    @NotBlank(message = "id不能为空", groups = {EditGroup.class})
    private String id;

    @ApiModelProperty(value = "创建时间")
    private Long createAt;

    @ApiModelProperty(value = "描述")
    @Size(max = 255, message = "描述长度不正确")
    private String desc;

    @ApiModelProperty(value = "转换器名称")
    @Size(max = 255, message = "转换器名称长度不正确")
    @NotBlank(message = "转换器名称不能为空", groups = {AddGroup.class})
    private String name;

    @ApiModelProperty(value = "脚本内容")
    @Size(max = 65535, message = "脚本内容长度不正确")
    private String script;

    @ApiModelProperty(value = "转换脚本类型")
    @Size(max = 255, message = "转换脚本类型长度不正确")
    private String typ;

    @ApiModelProperty(value = "所属性用户id")
    @Size(max = 255, message = "所属性用户id长度不正确")
    private String uid;

}
