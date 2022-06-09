package cc.iotkit.manager.controller;

import cc.iotkit.common.Constants;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.DeviceUtil;
import cc.iotkit.common.utils.UniqueIdUtil;
import cc.iotkit.comps.service.DeviceBehaviourService;
import cc.iotkit.dao.*;
import cc.iotkit.manager.model.query.DeviceQuery;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.manager.service.DeferredDataConsumer;
import cc.iotkit.manager.service.DeviceService;
import cc.iotkit.utils.AuthUtil;
import cc.iotkit.model.InvokeResult;
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
import org.springframework.web.context.request.async.DeferredResult;

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
    @Autowired
    DeferredDataConsumer deferredDataConsumer;

    @PostMapping(Constants.API_DEVICE.INVOKE_SERVICE)
    public InvokeResult invokeService(@PathVariable("deviceId") String deviceId,
                                      @PathVariable("service") String service,
                                      @RequestBody Map<String, Object> args) {
        if (StringUtils.isBlank(deviceId) || StringUtils.isBlank(service)) {
            throw new RuntimeException("deviceId/service is blank.");
        }
        return new InvokeResult(deviceService.invokeService(deviceId, service, args));
    }

    @PostMapping(Constants.API_DEVICE.SET_PROPERTIES)
    public InvokeResult setProperty(@PathVariable("deviceId") String deviceId,
                                    @RequestBody Map<String, Object> args) {
        return new InvokeResult(deviceService.setProperty(deviceId, args));
    }

    @PostMapping("/list/{size}/{page}")
    public Paging<DeviceInfo> getDevices(
            @PathVariable("size") int size,
            @PathVariable("page") int page,
            @RequestBody DeviceQuery query) {
        Criteria condition = new Criteria();

        String uid = AuthUtil.getUserId();
        if (!AuthUtil.isAdmin()) {
            //客户端用户使用绑定子用户查询
            if (AuthUtil.isClientUser()) {
                condition.and("subUid").elemMatch(new Criteria().is(uid));
            } else {
                condition.and("uid").is(uid);
            }
        }

        String pk = query.getProductKey();
        if (StringUtils.isNotBlank(pk)) {
            condition.and("productKey").is(pk);
        }
        String dn = query.getDeviceName();
        if (StringUtils.isNotBlank(dn)) {
            condition.and("deviceName").regex(".*" + dn + ".*");
        }
        String state = query.getState();
        if (StringUtils.isNotBlank(state)) {
            condition.and("state.online").is(state.equals("online"));
        }

        return deviceDao.find(condition, size, page);
    }

    @PostMapping("/create")
    public void createDevice(String productKey, String deviceName) {
        Optional<Product> productOpt = productRepository.findById(productKey);
        if (productOpt.isEmpty()) {
            throw new BizException("the product does not exist");
        }

        //生成设备密钥
        String chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
        int maxPos = chars.length();
        StringBuilder secret = new StringBuilder();
        for (var i = 0; i < 16; i++) {
            secret.append(chars.charAt((int) Math.floor(Math.random() * maxPos)));
        }

        DeviceInfo device = new DeviceInfo();
        device.setId(DeviceUtil.newDeviceId(deviceName));
        device.setUid(productOpt.get().getUid());
        device.setDeviceId(device.getId());
        device.setProductKey(productKey);
        device.setDeviceName(deviceName);
        device.setSecret(secret.toString());
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

    @GetMapping(Constants.API_DEVICE.DETAIL)
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

    @PostMapping("/{deviceId}/delete")
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

    /**
     * 消费设备信息消息（实时推送设备信息）
     */
    @GetMapping("/{deviceId}/consumer/{clientId}")
    public DeferredResult<ThingModelMessage> consumerDeviceInfo(
            @PathVariable("deviceId") String deviceId,
            @PathVariable("clientId") String clientId
    ) {
        String uid = AuthUtil.getUserId();
        DeviceInfo deviceInfo = deviceRepository.findByDeviceId(deviceId);
        dataOwnerService.checkOwner(deviceInfo);

        //按用户+客户端ID订阅
        return deferredDataConsumer.newConsumer(uid + clientId,
                Constants.HTTP_CONSUMER_DEVICE_INFO_TOPIC + deviceId);
    }
}
