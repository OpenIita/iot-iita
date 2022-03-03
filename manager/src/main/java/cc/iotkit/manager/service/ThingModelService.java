package cc.iotkit.manager.service;

import cc.iotkit.model.product.ThingModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ThingModelService {

    public Map<String, Object> paramsParse(ThingModel thingModel, String identifier, Map<?, ?> params) {
        Map<String, Object> parsedParams = new HashMap<>();
        ThingModel.Model model = thingModel.getModel();

        //属性设置
        if ("property/set".equals(identifier)) {
            List<ThingModel.Property> properties = model.getProperties();
            if (properties == null) {
                return parsedParams;
            }
            return parseProperties(properties, params);
        } else {
            //服务调用
            Map<String, ThingModel.Service> services = model.serviceMap();
            ThingModel.Service service = services.get(identifier);
            if (service == null) {
                return parsedParams;
            }
            List<ThingModel.Parameter> parameters = service.getInputData();
            return parseParams(parameters, params);
        }
    }

    private Map<String, Object> parseParams(List<ThingModel.Parameter> parameters, Map<?, ?> params) {
        Map<String, Object> parsed = new HashMap<>();
        parameters.forEach((p -> parseField(p.getIdentifier(), p.getDataType(), params, parsed)));
        return parsed;
    }

    private Map<String, Object> parseProperties(List<ThingModel.Property> properties, Map<?, ?> params) {
        Map<String, Object> parsed = new HashMap<>();
        properties.forEach((p -> parseField(p.getIdentifier(), p.getDataType(), params, parsed)));
        return parsed;
    }

    private void parseField(String identifier, ThingModel.DataType dataType, Map<?, ?> params, Map<String, Object> parsed) {
        Object val = params.get(identifier);
        if (val == null) {
            return;
        }
        Object result = dataType.parse(val);
        if (result == null) {
            return;
        }
        parsed.put(identifier, result);
    }

}
