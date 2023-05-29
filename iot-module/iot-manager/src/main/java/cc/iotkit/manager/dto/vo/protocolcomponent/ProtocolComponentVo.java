package cc.iotkit.manager.dto.vo.protocolcomponent;

import cc.iotkit.model.protocol.ProtocolComponent;

import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: jay
 * @Date: 2023/5/29 10:54
 * @Version: V1.0
 * @Description: 组件Vo
 */
@Data

@AutoMapper(target = ProtocolComponent.class)
public class ProtocolComponentVo implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 所属性用户id
     */
    @ApiModelProperty(value = "用户id")
    private String uid;

    @ApiModelProperty(value = "名称")
    private String name;


    @ApiModelProperty(value = "协议类型")

    private String protocol;

    @ApiModelProperty(value = "jar包")
    private String jarFile;

    @ApiModelProperty(value = "配置")
    private String config;

    @ApiModelProperty(value = "转换器")
    private String converter;



    @ApiModelProperty(value = "运行状态")
    private String state;

    private Long createAt;

    @ApiModelProperty(value = "脚本类型")
    private String scriptTyp;

    @ApiModelProperty(value = "脚本内容")
    private String script;
}
