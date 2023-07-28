package cc.iotkit.openapi.dto.vo;

import cc.iotkit.model.product.ProductModel;
import cc.iotkit.model.product.ThingModel;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@ApiModel(value = "OpenDevicePropertyVo")
@Data
@AutoMapper(target = ThingModel.class)
public class OpenDevicePropertyVo {

    @ApiModelProperty(value="设备属性")
    private Map<String, Object> property = new HashMap<>();

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "模型内容")
    private ThingModel.Model model;

    @ApiModelProperty(value = "产品key")
    private String productKey;
}
