package cc.iotkit.manager.service;

import cc.iotkit.common.exception.NotFoundException;
import cc.iotkit.common.exception.OfflineException;
import cc.iotkit.common.utils.UniqueIdUtil;
import cc.iotkit.comps.DeviceComponentManager;
import cc.iotkit.converter.ThingService;
import cc.iotkit.dao.DeviceRepository;
import cc.iotkit.dao.ThingModelMessageRepository;
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
    private DeviceComponentManager deviceComponentManager;
    @Autowired
    private ThingModelService thingModelService;
    @Autowired
    private ThingModelMessageRepository thingModelMessageRepository;

    public String invokeService(String deviceId, String service,
                                Map<String, Object> args) {
        return invokeService(deviceId, service, args, true);
    }

    public String invokeService(String deviceId, String service,
                                Map<String, Object> args, boolean checkOwner) {
        DeviceInfo device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new NotFoundException("device not found by deviceId"));

        if (checkOwner) {
            dataOwnerService.checkOwner(device);
        }
        if (!device.getState().isOnline()) {
            throw new OfflineException("device is offline");
        }

        ThingService<?> thingService = ThingService.builder()
                .mid(UniqueIdUtil.newRequestId())
                .productKey(device.getProductKey())
                .deviceName(device.getDeviceName())
                .type(ThingModelMessage.TYPE_SERVICE)
                .identifier(service)
                .params(args)
                .build();
        thingModelService.parseParams(thingService);

        deviceComponentManager.send(thingService);
        String mid = thingService.getMid();

        //保存设备日志
        ThingModelMessage thingModelMessage = ThingModelMessage.builder()
                .mid(mid)
                .deviceId(deviceId)
                .productKey(device.getProductKey())
                .deviceName(device.getDeviceName())
                .type(ThingModelMessage.TYPE_SERVICE)
                .identifier(service)
                .data(args)
                .occurred(System.currentTimeMillis())
                .time(System.currentTimeMillis())
                .build();
        thingModelMessageRepository.save(thingModelMessage);

        return mid;
    }

    public String setProperty(String deviceId, Map<String, Object> properties) {
        return setProperty(deviceId, properties, true);
    }

    public String setProperty(String deviceId, Map<String, Object> properties,
                              boolean checkOwner) {
        DeviceInfo device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new NotFoundException("device not found by deviceId"));

        if (checkOwner) {
            dataOwnerService.checkOwner(device);
        }
        if (!device.getState().isOnline()) {
            throw new OfflineException("device is offline");
        }

        ThingService<?> thingService = ThingService.builder()
                .mid(UniqueIdUtil.newRequestId())
                .productKey(device.getProductKey())
                .deviceName(device.getDeviceName())
                .type(ThingModelMessage.TYPE_PROPERTY)
                .identifier("set")
                .params(properties)
                .build();
        thingModelService.parseParams(thingService);

        deviceComponentManager.send(thingService);
        String mid = thingService.getMid();

        //保存设备日志
        ThingModelMessage thingModelMessage = ThingModelMessage.builder()
                .mid(mid)
                .deviceId(deviceId)
                .productKey(device.getProductKey())
                .deviceName(device.getDeviceName())
                .type(ThingModelMessage.TYPE_PROPERTY)
                .identifier("set")
                .data(properties)
                .occurred(System.currentTimeMillis())
                .time(System.currentTimeMillis())
                .build();
        thingModelMessageRepository.save(thingModelMessage);

        return mid;
    }

}
