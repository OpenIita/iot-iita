package cc.iotkit.manager.dto.bo.productmodel;

import cc.iotkit.model.product.ProductModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import cc.iotkit.common.api.BaseDto;
import cc.iotkit.common.validate.AddGroup;
import cc.iotkit.common.validate.EditGroup;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;


@ApiModel(value = "ProductModelBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ProductModel.class, reverseConvertGenerate = false)
public class ProductModelBo extends BaseDto  {

	private static final long serialVersionUID = -1L;

	@ApiModelProperty(value="id")
	private String id;

	@ApiModelProperty(value="型号")
	@Size(max = 255, message = "型号长度不正确")
    	private String model;

    	@ApiModelProperty(value="修改时间")
    	private Long modifyAt;

    	@ApiModelProperty(value="名称")
	@Size(max = 255, message = "名称长度不正确")
    	private String name;

    	@ApiModelProperty(value="产品Key")
	@Size(max = 255, message = "产品Key长度不正确")
    	private String productKey;

    	@ApiModelProperty(value="脚本内容")
	@Size(max = 65535, message = "脚本内容长度不正确")
    	private String script;

    	@ApiModelProperty(value="脚本状态")
	@Size(max = 255, message = "脚本状态长度不正确")
    	private String state;

    	@ApiModelProperty(value="脚本类型")
	@Size(max = 255, message = "脚本类型长度不正确")
    	private String type;

    }
