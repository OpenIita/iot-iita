package cc.iotkit.generator.dto.bo;

import cc.iotkit.common.api.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * @Author: jay
 * @Date: 2023/6/24 16:47
 * @Version: V1.0
 * @Description: 导入表Bo
 */
@Data
public class ImportTableBo extends BaseDto {

    @ApiModelProperty(value = "表名列表", notes = "表名列表")
    @NotEmpty(message = "表名列表不能为空")
    private List<String> tables;


}
