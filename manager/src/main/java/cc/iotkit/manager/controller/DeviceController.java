package cc.iotkit.manager.controller;

import cc.iotkit.dao.DeviceDao;
import cc.iotkit.dao.DeviceEventDao;
import cc.iotkit.dao.DeviceEventRepository;
import cc.iotkit.dao.DeviceRepository;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.manager.service.DeviceService;
import cc.iotkit.manager.utils.AuthUtil;
import cc.iotkit.model.device.message.DeviceEvent;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.PagingData;
import cc.iotkit.model.product.ThingModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceEventRepository deviceEventRepository;
    @Autowired
    private DeviceEventDao deviceEventDao;
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private DataOwnerService dataOwnerService;
    @Autowired
    private ProductController productController;

    @PostMapping("/{deviceId}/service/{service}")
    public String invokeService(@PathVariable("deviceId") String deviceId,
                                @PathVariable("service") String service,
                                @RequestBody Map<String, Object> args) {
        if (StringUtils.isBlank(deviceId) || StringUtils.isBlank(service)) {
            throw new RuntimeException("deviceId/service is blank.");
        }

        return deviceService.invokeService(deviceId, service, args);
    }

    @PostMapping("/{deviceId}/service/property/set")
    public String setProperty(@PathVariable("deviceId") String deviceId,
                              @RequestBody Map<String, Object> args) {
        return deviceService.setProperty(deviceId, args);
    }

    @PostMapping("/list")
    public PagingData<DeviceInfo> getDevices(int page,
                                             int size,
                                             String pk,
                                             Boolean online,
                                             String dn) {
        Criteria condition = new Criteria();
        if (!AuthUtil.isAdmin()) {
            condition.and("uid").is(AuthUtil.getUserId());
        }
        if (StringUtils.isNotBlank(pk)) {
            condition.and("productKey").is(pk);
        }
        if (StringUtils.isNotBlank(dn)) {
            condition.and("deviceName").regex(".*" + dn + ".*");
        }
        if (online != null) {
            condition.and("state.online").is(online);
        }
        return new PagingData<>(deviceDao.count(condition),
                deviceDao.find(condition, (page - 1) * size, size, Sort.Order.desc("createAt")));
    }

    @GetMapping("/{deviceId}/children")
    public List<DeviceInfo> getChildren(@PathVariable("deviceId") String deviceId) {
        return deviceRepository.findAll(Example.of(
                dataOwnerService.wrapExample(
                        DeviceInfo.builder()
                                .parentId(deviceId)
                                .build())));
    }

    @GetMapping("/{deviceId}")
    public DeviceInfo getDetail(@PathVariable("deviceId") String deviceId) {
        return dataOwnerService.checkOwner(deviceRepository.findById(deviceId).orElse(new DeviceInfo()));
    }

    @GetMapping("/{pk}/{dn}")
    public DeviceInfo getByPkDn(@PathVariable("pk") String pk,
                                @PathVariable("dn") String dn) {
        return dataOwnerService.checkOwner(
                deviceRepository.findOne(Example.of(DeviceInfo.builder()
                        .productKey(pk)
                        .deviceName(dn)
                        .build())).orElse(new DeviceInfo()));
    }

    @DeleteMapping("/{deviceId}")
    public void deleteDevice(@PathVariable("deviceId") String deviceId) {
        deviceId = getDetail(deviceId).getDeviceId();
        deviceRepository.deleteById(deviceId);
    }

    @PostMapping("/{deviceId}/events")
    public PagingData<DeviceEvent> events(@PathVariable("deviceId") String deviceId,
                                          int page,
                                          int limit,
                                          String type,
                                          String identifier) {
        Criteria condition = Criteria.where("deviceId").is(deviceId);
        if (StringUtils.isNotBlank(type)) {
            condition.and("type").is(type);
        }
        if (StringUtils.isNotBlank(identifier)) {
            condition.and("identifier").regex(".*" + identifier + ".*");
        }

        return new PagingData<>(deviceEventDao.count(condition),
                deviceEventDao.find(condition,
                        (page - 1) * limit, limit, Sort.Order.desc("createAt")));
    }

    @PostMapping("/{deviceId}/unbind")
    public void unbindDevice(@PathVariable("deviceId") String deviceId) {
        deviceId = getDetail(deviceId).getDeviceId();
        deviceService.unbindDevice(deviceId);
    }

    @GetMapping("/{deviceId}/thingModel")
    public ThingModel getThingModel(@PathVariable("deviceId") String deviceId) {
        DeviceInfo deviceInfo = getDetail(deviceId);
        return productController.getThingModel(deviceInfo.getProductKey());
    }
}
