package cc.iotkit.ruleengine.action;

import cc.iotkit.common.utils.UniqueIdUtil;
import cc.iotkit.comps.ComponentManager;
import cc.iotkit.converter.ThingService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DeviceActionService {

    @Autowired
    private ComponentManager componentManager;

    public String invoke(Service service) {
        String[] pkDn = service.getDevice().split("/");
        ThingService<Map<String, Object>> thingService = new ThingService<>();
        thingService.setMid(UniqueIdUtil.newRequestId());
        thingService.setProductKey(pkDn[0]);
        thingService.setDeviceName(pkDn[1]);
        thingService.setIdentifier(service.getIdentifier());
        thingService.setParams(service.parseInputData());
        componentManager.send(thingService);
        return thingService.getMid();
    }

    @Data
    public static class Service {

        private String device;

        private String identifier;

        private List<Parameter> inputData;

        public Map<String, Object> parseInputData() {
            Map<String, Object> data = new HashMap<>();
            for (Parameter p : inputData) {
                data.put(p.getIdentifier(), p.getValue());
            }
            return data;
        }

        @Data
        public static class Parameter {
            private String identifier;
            private Object value;
        }
    }

}
