/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.service;

import cc.iotkit.common.exception.NotFoundException;
import cc.iotkit.common.exception.OfflineException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.common.utils.UniqueIdUtil;
import cc.iotkit.comps.DeviceComponentManager;
import cc.iotkit.common.thing.ThingService;
import cc.iotkit.dao.DeviceConfigRepository;
import cc.iotkit.dao.DeviceInfoRepository;
import cc.iotkit.dao.ThingModelMessageRepository;
import cc.iotkit.model.device.DeviceConfig;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.virtualdevice.VirtualManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class DeviceService {

    @Autowired
    private DeviceInfoRepository deviceInfoRepository;
    @Autowired
    private DataOwnerService dataOwnerService;
    @Autowired
    private DeviceComponentManager deviceComponentManager;
    @Autowired
    private ThingModelService thingModelService;
    @Autowired
    private ThingModelMessageRepository thingModelMessageRepository;
    @Autowired
    private VirtualManager virtualManager;
    @Autowired
    private DeviceConfigRepository deviceConfigRepository;

    /**
     * 设备服务调用
     */
    public String invokeService(String deviceId, String service,
                                Map<String, Object> args) {
        return invokeService(deviceId, service, args, true);
    }

    /**
     * 设备服务调用
     */
    public String invokeService(String deviceId, String service,
                                Map<String, Object> args, boolean checkOwner) {
        DeviceInfo device = getAndCheckDevice(deviceId, checkOwner);

        return send(deviceId, device.getProductKey(), device.getDeviceName(),
                args, ThingModelMessage.TYPE_SERVICE, service);
    }

    /**
     * 设备属性设置
     */
    public String setProperty(String deviceId, Map<String, Object> properties) {
        return setProperty(deviceId, properties, true);
    }

    /**
     * 设备属性设置
     */
    public String setProperty(String deviceId, Map<String, Object> properties,
                              boolean checkOwner) {
        DeviceInfo device = getAndCheckDevice(deviceId, checkOwner);

        return send(deviceId, device.getProductKey(), device.getDeviceName(), properties,
                ThingModelMessage.TYPE_PROPERTY, ThingModelMessage.ID_PROPERTY_SET);
    }

    /**
     * 设备配置下发
     */
    public String sendConfig(String deviceId, boolean checkOwner) {
        DeviceInfo device = getAndCheckDevice(deviceId, checkOwner);

        DeviceConfig config = deviceConfigRepository.findByDeviceId(deviceId);
        Map data = JsonUtil.parse(config.getConfig(), Map.class);

        return send(deviceId, device.getProductKey(), device.getDeviceName(), data,
                ThingModelMessage.TYPE_CONFIG, ThingModelMessage.ID_CONFIG_SET);
    }

    /**
     * 设备配置下发
     */
    public String sendConfig(String deviceId) {
        return sendConfig(deviceId, true);
    }

    /**
     * 检查设备操作权限和状态
     */
    private DeviceInfo getAndCheckDevice(String deviceId, boolean checkOwner) {
        DeviceInfo device = deviceInfoRepository.findById(deviceId)
                .orElseThrow(() -> new NotFoundException("device not found by deviceId"));

        if (checkOwner) {
            dataOwnerService.checkOwner(device);
        }

        if (!device.getState().isOnline()) {
            throw new OfflineException("device is offline");
        }
        return device;
    }

    /**
     * 数据下发
     */
    private String send(String deviceId, String pk, String dn,
                        Object data, String type, String identifier) {
        ThingService<?> thingService = ThingService.builder()
                .mid(UniqueIdUtil.newRequestId())
                .productKey(pk)
                .deviceName(dn)
                .type(type)
                .identifier(identifier)
                .params(data)
                .build();
        if (!type.equals(ThingModelMessage.TYPE_CONFIG)) {
            //非配置下发需要做物模型转换
            thingModelService.parseParams(thingService);
        }

        if (virtualManager.isVirtual(deviceId)) {
            //虚拟设备指令下发
            virtualManager.send(thingService);
        } else {
            //设备指令下发
            deviceComponentManager.send(thingService);
        }
        String mid = thingService.getMid();

        //保存设备日志
        ThingModelMessage thingModelMessage = ThingModelMessage.builder()
                .mid(mid)
                .deviceId(deviceId)
                .productKey(pk)
                .deviceName(dn)
                .type(type)
                .identifier(identifier)
                .data(data)
                .occurred(System.currentTimeMillis())
                .time(System.currentTimeMillis())
                .build();
        thingModelMessageRepository.save(thingModelMessage);

        return mid;
    }

}