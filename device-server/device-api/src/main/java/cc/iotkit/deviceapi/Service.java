package cc.iotkit.deviceapi;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Service {

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
