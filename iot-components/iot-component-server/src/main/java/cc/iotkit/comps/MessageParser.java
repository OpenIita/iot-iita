package cc.iotkit.comps;

import cc.iotkit.comp.model.DeviceState;
import cc.iotkit.comp.model.RegisterInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class MessageParser {

    public static RegisterInfo parseRegisterInfo(Map value) {
        RegisterInfo registerInfo = new RegisterInfo();
        try {
            List<RegisterInfo.SubDevice> subDevices = new ArrayList<>();
            Object objSubDevices = value.get(RegisterInfo.Fields.subDevices);
            if (objSubDevices instanceof List) {
                for (Object item : (List) objSubDevices) {
                    subDevices.add(parse(new RegisterInfo.SubDevice(), item));
                }
            }
            registerInfo.setSubDevices(subDevices);
            return RegisterInfo.builder()
                    .deviceName(Objects.toString(value.get(RegisterInfo.Fields.deviceName), ""))
                    .productKey(Objects.toString(value.get(RegisterInfo.Fields.productKey), ""))
                    .model(Objects.toString(value.get(RegisterInfo.Fields.model), ""))
                    .subDevices(subDevices)
                    .build();
        } catch (Throwable e) {
            log.error("parse bean from Value error", e);
            return null;
        }
    }

    @SneakyThrows
    public static DeviceState parseDeviceState(Map value) {
        return DeviceState.builder()
                .deviceName(Objects.toString(value.get(DeviceState.Fields.deviceName), ""))
                .productKey(Objects.toString(value.get(DeviceState.Fields.productKey), ""))
                .state(Objects.toString(value.get(DeviceState.Fields.state), ""))
                .parent(
                        parse(new DeviceState.Parent(), value.get(DeviceState.Fields.parent))
                )
                .build();
    }

    @SneakyThrows
    public static <T> T parse(T obj, Object map) {
        if (!(map instanceof Map)) {
            return null;
        }

        BeanUtils.populate(obj, (Map<String, ? extends Object>) map);
        return obj;
    }

}
