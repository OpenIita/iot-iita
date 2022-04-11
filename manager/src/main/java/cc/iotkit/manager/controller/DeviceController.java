package cc.iotkit.manager.controller;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.DeviceUtil;
import cc.iotkit.common.utils.UniqueIdUtil;
import cc.iotkit.comps.service.DeviceBehaviourService;
import cc.iotkit.dao.*;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.manager.service.DeviceService;
import cc.iotkit.manager.utils.AuthUtil;
import cc.iotkit.model.Paging;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.DeviceProperty;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.product.ThingModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private DataOwnerService dataOwnerService;
    @Autowired
    private ProductController productController;
    @Lazy
    @Autowired
    private ThingModelMessageDao thingModelMessageDao;
    @Lazy
    @Autowired
    private DevicePropertyDao devicePropertyDao;
    @Autowired
    private DeviceBehaviourService behaviourService;

    @PostMapping("/{deviceId}/service/{service}")
    public String invokeService(@PathVariable("deviceId") String deviceId,
                                @PathVariable("service") String service,
                                @RequestBody Map<String, Object> args) {
        if (StringUtils.isBlank(deviceId) || StringUtils.isBlank(service)) {
            throw new RuntimeException("deviceId/service is blank.");
        }
        dataOwnerService.checkWriteRole();
        return deviceService.invokeService(deviceId, service, args);
    }

    @PostMapping("/{deviceId}/service/property/set")
    public String setProperty(@PathVariable("deviceId") String deviceId,
                              @RequestBody Map<String, Object> args) {
        dataOwnerService.checkWriteRole();
        return deviceService.setProperty(deviceId, args);
    }

    @PostMapping("/list")
    public Paging<DeviceInfo> getDevices(int page,
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

        return deviceDao.find(condition, size, page);
    }

    @PostMapping("/create")
    public void createDevice(String productKey, String deviceName) {
        Optional<Product> productOpt = productRepository.findById(productKey);
        if (!productOpt.isPresent()) {
            throw new BizException("the product does not exist");
        }

        DeviceInfo device = new DeviceInfo();
        device.setId(DeviceUtil.newDeviceId(deviceName));
        device.setUid(productOpt.get().getUid());
        device.setDeviceId(device.getId());
        device.setProductKey(productKey);
        device.setDeviceName(deviceName);
        device.setState(new DeviceInfo.State(false, null, null));
        device.setCreateAt(System.currentTimeMillis());

        deviceRepository.save(device);
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

    @PostMapping("/{deviceId}/logs/{size}/{page}")
    public Paging<ThingModelMessage> logs(
            @PathVariable("deviceId") String deviceId,
            @PathVariable("size") int size,
            @PathVariable("page") int page,
            String type, String identifier) {
        return thingModelMessageDao.findByTypeAndIdentifier(deviceId, type, identifier, page, size);
    }

    @GetMapping("/{deviceId}/property/{name}/{start}/{end}")
    public List<DeviceProperty> getPropertyHistory(
            @PathVariable("deviceId") String deviceId,
            @PathVariable("name") String name,
            @PathVariable("start") long start,
            @PathVariable("end") long end) {
        return devicePropertyDao.findDevicePropertyHistory(deviceId, name, start, end);
    }

    @PostMapping("/{deviceId}/unbind")
    public void unbindDevice(@PathVariable("deviceId") String deviceId) {
        deviceId = getDetail(deviceId).getDeviceId();
//        deviceService.unbindDevice(deviceId);
    }

    @GetMapping("/{deviceId}/thingModel")
    public ThingModel getThingModel(@PathVariable("deviceId") String deviceId) {
        DeviceInfo deviceInfo = getDetail(deviceId);
        return productController.getThingModel(deviceInfo.getProductKey());
    }

    @PostMapping("/{deviceId}/tag/add")
    public void addTag(@PathVariable("deviceId") String deviceId,
                       DeviceInfo.Tag tag) {
        DeviceInfo device = deviceRepository.findByDeviceId(deviceId);
        dataOwnerService.checkOwner(device);
        deviceDao.updateTag(deviceId, tag);
    }

    @PostMapping("/{deviceId}/simulateSend")
    public void simulateSend(
            @PathVariable("deviceId") String deviceId,
            @RequestBody ThingModelMessage message) {
        DeviceInfo device = deviceRepository.findByDeviceId(deviceId);
        dataOwnerService.checkOwner(device);

        message.setMid(UniqueIdUtil.newRequestId());
        message.setOccurred(System.currentTimeMillis());
        message.setTime(System.currentTimeMillis());
        behaviourService.reportMessage(message);
    }
}
