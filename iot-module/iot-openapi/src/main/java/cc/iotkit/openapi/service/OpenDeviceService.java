package cc.iotkit.openapi.service;

import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.openapi.dto.bo.device.OpenapiDeviceBo;

/**
 * @Author: dsy
 * @Date: 2023/7/24 11:05
 * @Version: V1.0
 * @Description: openapi设备服务接口
 */
public interface OpenDeviceService {
    DeviceInfo getDetail(OpenapiDeviceBo bo);
}
