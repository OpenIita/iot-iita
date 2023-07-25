package cc.iotkit.openapi.service.impl;

import cc.iotkit.data.manager.IDeviceInfoData;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.openapi.dto.bo.device.OpenapiDeviceBo;
import cc.iotkit.openapi.service.OpenDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class OpenDeviceServiceImpl implements OpenDeviceService {

    @Autowired
    @Qualifier("deviceInfoDataCache")
    private IDeviceInfoData deviceInfoData;

    @Override
    public DeviceInfo getDetail(OpenapiDeviceBo data) {
        DeviceInfo deviceInfo = deviceInfoData.findByProductKeyAndDeviceName(data.getProductKey(), data.getDeviceName());
        deviceInfo.setProperty(deviceInfoData.getProperties(deviceInfo.getDeviceId()));
        return deviceInfo;
    }
}
