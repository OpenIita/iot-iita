package cc.iotkit.manager.dto.bo.thingmodel;

import cc.iotkit.model.product.ThingModel;
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


@ApiModel(value = "ThingModelBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ThingModel.class, reverseConvertGenerate = false)
public class ThingModelBo extends BaseDto  {

	private static final long serialVersionUID = -1L;

	@ApiModelProperty(value="模型内容")
	@Size(max = 65535, message = "模型内容长度不正确")
    	private String model;

    	@ApiModelProperty(value="产品key")
	@Size(max = 255, message = "产品key长度不正确")
    	private String productKey;

    }
