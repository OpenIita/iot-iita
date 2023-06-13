package cc.iotkit.data.model;

import cc.iotkit.model.protocol.ProtocolComponent;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "protocol_component")
@AutoMapper(target = ProtocolComponent.class)
public class TbProtocolComponent {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    private String id;

    /**
     * 所属性用户id
     */
    @ApiModelProperty(value = "所属性用户id")
    private String uid;

    @ApiModelProperty(value = "组件名称")
    private String name;

    @ApiModelProperty(value = "组件类型")

    private String type;

    @ApiModelProperty(value = "通讯协议")

    private String protocol;

    @ApiModelProperty(value = "jar包")

    private String jarFile;

    @Column(columnDefinition = "text")
    private String config;

    @ApiModelProperty(value = "转换脚本")

    private String converter;

    @ApiModelProperty(value = "转换器类型")

    private String converType;

    @ApiModelProperty(value = "运行状态")

    private String state;

    @ApiModelProperty(value = "创建时间")

    private Long createAt;

    @ApiModelProperty(value = "通讯脚本语言类型")

    private String scriptTyp;

    @ApiModelProperty(value = "脚本内容")

    @Column(columnDefinition = "text")//设置映射为text类型
    private String script;

}
