package cc.iotkit.manager.dto.vo.protocolconverter;

import cc.iotkit.model.protocol.ProtocolConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;


@ApiModel(value = "ProtocolConverterVo")
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ProtocolConverter.class)

public class ProtocolConverterVo implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "")
    @ExcelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "创建时间")
    @ExcelProperty(value = "创建时间")
    private Long createAt;

    @ApiModelProperty(value = "描述")
    @ExcelProperty(value = "描述")
    private String desc;

    @ApiModelProperty(value = "转换器名称")
    @ExcelProperty(value = "转换器名称")
    private String name;

    @ApiModelProperty(value = "脚本内容")
    @ExcelProperty(value = "脚本内容")
    private String script;

    @ApiModelProperty(value = "转换脚本类型")
    @ExcelProperty(value = "转换脚本类型")
    private String typ;

    @ApiModelProperty(value = "所属性用户id")
    @ExcelProperty(value = "所属性用户id")
    private String uid;


}
