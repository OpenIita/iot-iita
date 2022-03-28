package cc.iotkit.manager.service;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.exception.NotFoundException;
import cc.iotkit.common.utils.UniqueIdUtil;
import cc.iotkit.comps.ComponentManager;
import cc.iotkit.converter.ThingService;
import cc.iotkit.dao.DeviceRepository;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.ThingModelMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DataOwnerService dataOwnerService;
    @Autowired
    private ComponentManager componentManager;

    public String invokeService(String deviceId, String service, Map<String, Object> args) {
        DeviceInfo device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new NotFoundException("device not found by deviceId"));

        dataOwnerService.checkOwner(device);
        if (!device.getState().isOnline()) {
            throw new BizException("device is offline");
        }

        ThingService<?> thingService = ThingService.builder()
                .mid(UniqueIdUtil.newRequestId())
                .productKey(device.getProductKey())
                .deviceName(device.getDeviceName())
                .type(ThingModelMessage.TYPE_SERVICE)
                .identifier(service)
                .params(args)
                .build();
        componentManager.send(thingService);
        return thingService.getMid();
    }

    public String setProperty(String deviceId, Map<String, Object> properties) {
        DeviceInfo device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new NotFoundException("device not found by deviceId"));

        dataOwnerService.checkOwner(device);
        if (!device.getState().isOnline()) {
            throw new BizException("device is offline");
        }

        ThingService<?> thingService = ThingService.builder()
                .mid(UniqueIdUtil.newRequestId())
                .productKey(device.getProductKey())
                .deviceName(device.getDeviceName())
                .type(ThingModelMessage.TYPE_PROPERTY)
                .identifier("set")
                .params(properties)
                .build();
        componentManager.send(thingService);
        return thingService.getMid();
    }

}
