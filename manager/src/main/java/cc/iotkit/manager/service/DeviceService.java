package cc.iotkit.manager.service;

import cc.iotkit.common.exception.NotFoundException;
import cc.iotkit.dao.DeviceCache;
import cc.iotkit.dao.DeviceEventRepository;
import cc.iotkit.dao.DeviceRepository;
import cc.iotkit.dao.ThingModelRepository;
import cc.iotkit.deviceapi.IDeviceManager;
import cc.iotkit.model.device.DeviceInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DeviceService {

    @Autowired
    private DeviceCache deviceCache;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private ThingModelRepository thingModelRepository;
    @Autowired
    private ThingModelService thingModelService;
    @Autowired
    private DeviceEventRepository deviceEventRepository;
    @Autowired
    private DataOwnerService dataOwnerService;
    @Autowired
    private IDeviceManager deviceManager;

    public String invokeService(String deviceId, String service, Map<String, Object> args) {
        DeviceInfo device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new NotFoundException("device not found by deviceId"));

        dataOwnerService.checkOwner(device);
        return this.deviceManager.invokeService(deviceId, service, args);
    }

    public String setProperty(String deviceId, Map<String, Object> properties) {
        DeviceInfo device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new NotFoundException("device not found by deviceId"));

        dataOwnerService.checkOwner(device);
        return deviceManager.setProperty(deviceId, properties);
    }

    public void unbindDevice(String deviceId) {
        deviceManager.unbind(deviceId);
    }

    public List<DeviceInfo> findDevices(DeviceInfo form) {
        return deviceRepository.findAll(Example.of(form));
    }

    public long count(DeviceInfo form) {
        return deviceRepository.count(Example.of(form));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class CmdRequest {
        private String id;
        private Object params;
    }
}
