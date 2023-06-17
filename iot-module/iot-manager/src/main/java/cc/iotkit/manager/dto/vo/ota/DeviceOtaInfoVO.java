package cc.iotkit.manager.dto.vo.ota;

import cc.iotkit.model.device.DeviceInfo;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 石恒
 * @Date: 2023/6/17 20:49
 * @Description:
 */
@Data
@ExcelIgnoreUnannotated
@ApiModel(value = "DeviceOtaInfoVO")
@AutoMapper(target = DeviceOtaInfoVO.class)
public class DeviceOtaInfoVO implements Serializable {
    private Long id;

    private Integer step;

    private String taskId;

    private String desc;

    private String version;

    private String module;

    private String deviceId;

    private String productKey;

    private String deviceName;
}
