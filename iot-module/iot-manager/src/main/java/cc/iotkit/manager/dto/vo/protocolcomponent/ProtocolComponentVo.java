package cc.iotkit.manager.dto.vo.protocolcomponent;

import cc.iotkit.model.protocol.ProtocolComponent;
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


@ApiModel(value = "ProtocolComponentVo")
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ProtocolComponent.class)

public class ProtocolComponentVo implements Serializable  {

	private static final long serialVersionUID = -1L;

	@ApiModelProperty(value="")
    @ExcelProperty(value = "")
		private String id;

	@ApiModelProperty(value="")
    @ExcelProperty(value = "")
		private String config;

	@ApiModelProperty(value="转换器类型")
    @ExcelProperty(value = "转换器类型")
		private String converType;

	@ApiModelProperty(value="转换脚本")
    @ExcelProperty(value = "转换脚本")
		private String converter;

	@ApiModelProperty(value="创建时间")
    @ExcelProperty(value = "创建时间")
		private Long createAt;

	@ApiModelProperty(value="jar包")
    @ExcelProperty(value = "jar包")
		private String jarFile;

	@ApiModelProperty(value="组件名称")
    @ExcelProperty(value = "组件名称")
		private String name;

	@ApiModelProperty(value="通讯协议")
    @ExcelProperty(value = "通讯协议")
		private String protocol;

	@ApiModelProperty(value="脚本内容")
    @ExcelProperty(value = "脚本内容")
		private String script;

	@ApiModelProperty(value="通讯脚本语言类型")
    @ExcelProperty(value = "通讯脚本语言类型")
		private String scriptTyp;

	@ApiModelProperty(value="运行状态")
    @ExcelProperty(value = "运行状态")
		private String state;

	@ApiModelProperty(value="组件类型")
    @ExcelProperty(value = "组件类型")
		private String type;

	@ApiModelProperty(value="所属性用户id")
    @ExcelProperty(value = "所属性用户id")
		private String uid;



}
