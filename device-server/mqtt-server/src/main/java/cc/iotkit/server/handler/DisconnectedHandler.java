package cc.iotkit.server.handler;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.server.dao.DeviceRepository;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.server.service.DeviceService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DisconnectedHandler {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceRepository deviceRepository;

    public void handler(String msg) {
        Disconnected disconnected = JsonUtil.parse(msg, new TypeReference<Disconnected>() {
        });
        String clientId = disconnected.getClientid();
        String[] parts = clientId.split("_");
        if (parts.length < 2) {
            return;
        }
        String pk = parts[0];
        String dn = parts[1];
        log.info("gateway disconnected, offline,pk:{},dn:{}", pk, dn);

        DeviceInfo example = new DeviceInfo();
        example.setProductKey(pk);
        example.setDeviceName(dn);
        DeviceInfo device = deviceRepository.findOne(Example.of(example)).orElse(new DeviceInfo());
        if (device.getDeviceId() == null) {
            log.error("no device found by pk:{} and dn:{}", pk, dn);
            return;
        }
        deviceService.offline(pk, dn);

        example = new DeviceInfo();
        example.setParentId(device.getDeviceId());
        //子设备下线
        List<DeviceInfo> children = deviceRepository.findAll(Example.of(example));
        children.forEach(c -> deviceService.offline(c.getProductKey(), c.getDeviceName()));
    }

    @Data
    private static class Disconnected {
        private String reason;
        private String clientid;
        private String username;
        private String peername;
        private String sockname;
    }

}
