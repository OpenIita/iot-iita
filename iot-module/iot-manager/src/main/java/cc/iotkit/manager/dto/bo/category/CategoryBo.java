package cc.iotkit.manager.dto.bo.category;

import cc.iotkit.model.product.Category;
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


@ApiModel(value = "CategoryBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Category.class, reverseConvertGenerate = false)
public class CategoryBo extends BaseDto  {

	private static final long serialVersionUID = -1L;


	@ApiModelProperty(value="id")
	private String id;

	@ApiModelProperty(value="分类描述")
    	private Long createAt;

    	@ApiModelProperty(value="分类名称")
	@Size(max = 255, message = "分类名称长度不正确")
    	private String name;

    }
