package cc.iotkit.server.service;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.exception.NotFoundException;
import cc.iotkit.common.exception.OfflineException;
import cc.iotkit.common.utils.DeviceUtil;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.common.utils.UniqueIdUtil;
import cc.iotkit.deviceapi.IDeviceManager;
import cc.iotkit.deviceapi.IDeviceService;
import cc.iotkit.deviceapi.Service;
import cc.iotkit.model.device.message.DeviceEvent;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.product.ThingModel;
import cc.iotkit.model.mq.Request;
import cc.iotkit.server.dao.DeviceDao;
import cc.iotkit.server.dao.DeviceEventRepository;
import cc.iotkit.server.dao.DeviceRepository;
import cc.iotkit.server.dao.ThingModelRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class DeviceService implements IDeviceManager, IDeviceService {

    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private ThingModelRepository thingModelRepository;
    @Autowired
    private ThingModelService thingModelService;
    @Autowired
    private DeviceEventRepository deviceEventRepository;

    @Autowired
    private IMqttSender mqttSender;

    private static final String identifier_set = "property/set";

    @Override
    public DeviceInfo register(String parentId, String productKey, String deviceName, String model) {
        DeviceInfo device = new DeviceInfo();
        device.setParentId(parentId);
        device.setProductKey(productKey);
        device.setDeviceName(deviceName);
        device.setModel(model);

        DeviceInfo parentDevice = deviceDao.getByDeviceId(parentId);
        if (parentDevice == null) {
            throw new BizException("Parent device does not exist");
        }
        String uid = parentDevice.getUid();

        DeviceInfo deviceInfo = deviceDao.getByPkAndDn(productKey, deviceName);
        if (deviceInfo != null) {
            device.setId(deviceInfo.getId());
            device.setDeviceId(deviceInfo.getDeviceId());
            device.setUid(uid);
            deviceDao.updateDevice(device);
            log.info("device register update:{}", JsonUtil.toJsonString(device));
            return deviceInfo;
        }

        String deviceId = newDeviceId(deviceName);

        device.setId(deviceId);
        device.setDeviceId(deviceId);
        device.setUid(uid);
        deviceDao.addDevice(device);
        log.info("device registered:{}", JsonUtil.toJsonString(device));
        return device;
    }

    @Override
    public void unbind(String deviceId) {
        log.info("start unbind device,deviceId:{}", deviceId);

        DeviceInfo device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("no device found by deviceId:" + deviceId));

        String gatewayId = device.getParentId();
        DeviceInfo gateway = deviceRepository.findById(gatewayId)
                .orElseThrow(() -> new RuntimeException("no device found by deviceId:" + deviceId));

        //数据库解绑
        device.setParentId("");
        deviceRepository.save(device);

        //网关注销
        String topic = "/sys/" + gateway.getProductKey() + "/" + gateway.getDeviceName() + "/c/service/deregister";
        String requestId = UniqueIdUtil.newRequestId();
        Map<String, Object> params = new HashMap<>();
        params.put("productKey", device.getProductKey());
        params.put("deviceName", device.getDeviceName());
        CmdRequest request = new CmdRequest(requestId, params);
        String msg = JsonUtil.toJsonString(request);
        log.info("start send mqtt msg,topic:{},payload:{}", topic, msg);
        mqttSender.sendToMqtt(topic, msg);
    }

    @Override
    public String invoke(Service service) {
        return sendMsg(service);
    }

    @Override
    public String setProperty(String deviceId, @RequestBody Map<String, Object> properties) {
        return sendMsg(deviceId, identifier_set, properties);
    }

    @Override
    public String invokeService(String deviceId, String identifier, Map<String, Object> properties) {
        return sendMsg(deviceId, identifier, properties);
    }

    public void offline(String pk, String dn) {
        DeviceInfo device = new DeviceInfo();
        device.setProductKey(pk);
        device.setDeviceName(dn);

        device.getState().setOnline(false);
        device.getState().setOfflineTime(System.currentTimeMillis());
        deviceDao.updateDeviceByPkAndDn(device);
        log.info("device offline,pk:{},dn:{}", pk, dn);
    }

    public String sendMsg(String deviceId, String service, Map<String, Object> args) {
        DeviceInfo device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new NotFoundException("device not found by deviceId"));

        return this.sendMsg(device, service, args);
    }

    public String sendMsg(DeviceInfo device, String service, Map<String, Object> args) {
        if (device.getState() == null || device.getState().getOnline() != Boolean.TRUE) {
            throw new OfflineException("device is offline");
        }

        String pk = device.getProductKey();
        String dn = device.getDeviceName();

        ThingModel thingModel = thingModelRepository.findById(pk)
                .orElseThrow(() -> new NotFoundException("device thingModel not found"));

        String topic = "/sys/" + pk + "/" + dn + "/c/service/" + service;
        String requestId = UniqueIdUtil.newRequestId();

        //参数类型转换
        args = thingModelService.paramsParse(thingModel, service, args);

        CmdRequest request = new CmdRequest(requestId, args);
        String msg = JsonUtil.toJsonString(request);
        log.info("start send mqtt msg,topic:{},payload:{}", topic, msg);
        mqttSender.sendToMqtt(topic, msg);

        //记录下行日志
        DeviceEvent deviceEvent = DeviceEvent.builder()
                .deviceId(device.getDeviceId())
                .identifier(service.replace("property/set", "propertySet"))
                .type("service")
                .request(new Request<>(requestId, args))
                .createAt(System.currentTimeMillis())
                .build();
        deviceEventRepository.save(deviceEvent);

        return requestId;
    }

    public String sendMsg(Service service) {
        DeviceUtil.PkDn pkDn = DeviceUtil.getPkDn(service.getDevice());
        DeviceInfo deviceInfo = deviceDao.getByPkAndDn(pkDn.getProductKey(), pkDn.getDeviceName());
        if ("set".equals(service.getIdentifier())) {
            return sendMsg(deviceInfo.getDeviceId(), identifier_set, service.parseInputData());
        } else {
            return sendMsg(deviceInfo.getDeviceId(), service.getIdentifier(), service.parseInputData());
        }
    }

    /**
     * 1-13位	时间戳
     * 14-29位	deviceNae，去除非字母和数字，不足16位补0，超过16位的mac取后16位，共16位
     * 30-31位	mac长度，共2位
     * 32位	随机一个0-f字符
     */
    private static String newDeviceId(String deviceNae) {
        int maxDnLen = 16;
        String dn = deviceNae.replaceAll("[^0-9A-Za-z]", "");
        if (dn.length() > maxDnLen) {
            dn = dn.substring(dn.length() - maxDnLen);
        } else {
            dn = (dn + "00000000000000000000").substring(0, maxDnLen);
        }
        String len = StringUtils.leftPad(deviceNae.length() + "", 2, '0');
        String rnd = Integer.toHexString(RandomUtils.nextInt(0, 16));
        return (System.currentTimeMillis() + "0" + dn + len + rnd).toLowerCase();
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class CmdRequest {
        private String id;
        private Object params;
    }
}
