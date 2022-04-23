package cc.iotkit.manager.controller.aligenie;

import cc.iotkit.common.Constants;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.UniqueIdUtil;
import cc.iotkit.dao.*;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.manager.service.DeviceService;
import cc.iotkit.model.UserInfo;
import cc.iotkit.model.aligenie.AligenieDevice;
import cc.iotkit.model.aligenie.AligenieProduct;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.device.message.ThingModelMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.impl.schema.JSONSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/aligenieDevice")
public class AligenieDeviceController {

    @Autowired
    private AligenieDeviceRepository aligenieDeviceRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private DataOwnerService ownerService;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private AligenieProductRepository aligenieProductRepository;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceDao deviceDao;
    @Value("${app.aligenie.push.device}")
    private String pushDevice;
    @Value("${pulsar.broker}")
    private String pulsarBrokerUrl;

    private Producer<ThingModelMessage> deviceMessageProducer;

    @PostConstruct
    public void init() throws PulsarClientException {
        //初始化pulsar客户端
        PulsarClient client = PulsarClient.builder()
                .serviceUrl(pulsarBrokerUrl)
                .build();
        deviceMessageProducer = client.newProducer(JSONSchema.of(ThingModelMessage.class))
                .topic("persistent://iotkit/default/" + Constants.THING_MODEL_MESSAGE_TOPIC)
                .create();
    }

    @GetMapping("/list/{uid}")
    public List<AligenieDevice> getDevices(@PathVariable("uid") String uid) {
        UserInfo user = userInfoRepository.findById(uid).get();
        ownerService.checkOwner(user);
        return aligenieDeviceRepository.findByUid(uid);
    }

    @PostMapping("/bind/{uid}")
    public void bind(@PathVariable("uid") String uid,
                     @RequestBody List<Device> devices) throws PulsarClientException {
        Optional<UserInfo> optUser = userInfoRepository.findById(uid);
        if (!optUser.isPresent()) {
            throw new BizException("user does not exist");
        }
        UserInfo user = optUser.get();
        ownerService.checkOwner(user);
        String token = "";
        List<AligenieDevice> aligenieDevices = aligenieDeviceRepository.findByUid(uid);
        //先清除待删除的标签
        for (AligenieDevice alDevice : aligenieDevices) {
            deviceDao.setTagNull(alDevice.getDeviceId(), "aligenie");
            token = alDevice.getToken();
        }
        //删除原有的设备
        aligenieDeviceRepository.deleteByUid(uid);

        for (Device device : devices) {
            DeviceInfo deviceInfo = deviceRepository.findById(device.getDeviceId()).get();
            AligenieProduct product = aligenieProductRepository.findByProductKey(deviceInfo.getProductKey());
            aligenieDeviceRepository.save(AligenieDevice.builder()
                    .uid(user.getId())
                    .token(token)
                    .deviceId(device.getDeviceId())
                    .productId(product.getProductId())
                    .spaceName("客厅")
                    .name(device.getName())
                    .build());

            //设置天猫精灵接入标签
            deviceDao.updateTag(device.getDeviceId(),
                    new DeviceInfo.Tag("aligenie", "天猫精灵接入", "是"));
        }

        DeviceInfo deviceInfo = deviceRepository.findByDeviceId(pushDevice);
        if (deviceInfo == null) {
            return;
        }

        Map<String, Object> uidData = new HashMap<>();
        uidData.put("uid", uid);
        deviceMessageProducer.send(ThingModelMessage.builder()
                .deviceId(pushDevice)
                .productKey(deviceInfo.getProductKey())
                .deviceName(deviceInfo.getDeviceName())
                .type(ThingModelMessage.TYPE_EVENT)
                .identifier("userDevicesChange")
                .mid(UniqueIdUtil.newRequestId())
                .data(uidData)
                .build());
    }

    @Data
    public static class Device {
        private String deviceId;
        private String name;
    }

}
