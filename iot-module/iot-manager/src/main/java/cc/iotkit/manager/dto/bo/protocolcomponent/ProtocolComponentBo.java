package cc.iotkit.manager.dto.bo.protocolcomponent;

import cc.iotkit.model.protocol.ProtocolComponent;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import jakarta.validation.constraints.Size;


import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.common.validate.AddGroup;
import cc.iotkit.common.validate.EditGroup;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;


@ApiModel(value = "ProtocolComponentBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ProtocolComponent.class, reverseConvertGenerate = false)
public class ProtocolComponentBo extends BaseDto {
    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    @Size(max = 65535, message = "长度不正确")
    private String config;

    @ApiModelProperty(value = "转换器类型")
    @Size(max = 255, message = "转换器类型长度不正确")
    private String converType;

    @ApiModelProperty(value = "转换脚本")
    @Size(max = 255, message = "转换脚本长度不正确")
    private String converter;

    @ApiModelProperty(value = "创建时间")
    private Long createAt;

    @ApiModelProperty(value = "jar包")
    @Size(max = 255, message = "jar包长度不正确")
    private String jarFile;

    @ApiModelProperty(value = "组件名称")
    @Size(max = 255, message = "组件名称长度不正确")
    private String name;

    @ApiModelProperty(value = "通讯协议")
    @Size(max = 255, message = "通讯协议长度不正确")
    private String protocol;

    @ApiModelProperty(value = "脚本内容")
    @Size(max = 65535, message = "脚本内容长度不正确")
    private String script;

    @ApiModelProperty(value = "通讯脚本语言类型")
    @Size(max = 255, message = "通讯脚本语言类型长度不正确")
    private String scriptTyp;

    @ApiModelProperty(value = "运行状态")
    @Size(max = 255, message = "运行状态长度不正确")
    private String state;

    @ApiModelProperty(value = "组件类型")
    @Size(max = 255, message = "组件类型长度不正确")
    private String type;

    @ApiModelProperty(value = "所属性用户id")
    @Size(max = 255, message = "所属性用户id长度不正确")
    private String uid;

}
