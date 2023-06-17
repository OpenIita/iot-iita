package cc.iotkit.manager.dto.bo.ota;

import cc.iotkit.common.api.BaseDto;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: 石恒
 * @Date: 2023/6/16 21:13
 * @Description:
 */
@ApiModel(value = "DeviceUpgradeBo")
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DeviceUpgradeBo.class, reverseConvertGenerate = false)
public class DeviceUpgradeBo extends BaseDto {
    private static final long serialVersionUID = -1L;
    private String deviceId;
    private Long otaId;
}
