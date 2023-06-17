package cc.iotkit.manager.dto.bo.ota;

import cc.iotkit.common.api.BaseDto;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: 石恒
 * @Date: 2023/6/17 20:45
 * @Description:
 */
@Data
@ApiModel(value = "DeviceOtaInfoBo")
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DeviceOtaInfoBo.class, reverseConvertGenerate = false)
public class DeviceOtaInfoBo extends BaseDto {

    private String taskId;

    private String version;

    private String deviceId;

    private String productKey;

    private String deviceName;
}
