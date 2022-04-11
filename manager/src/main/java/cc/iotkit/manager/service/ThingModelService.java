package cc.iotkit.manager.service;

import cc.iotkit.converter.ThingService;
import cc.iotkit.dao.ThingModelRepository;
import cc.iotkit.model.product.ThingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ThingModelService {
    @Autowired
    private ThingModelRepository thingModelRepository;

    public void parseParams(ThingService service) {
        ThingModel thingModel = thingModelRepository.findByProductKey(service.getProductKey());
        thingModel.getModel();
        ThingModel.Model model = thingModel.getModel();

        String type = service.getType();
        String identifier = service.getIdentifier();
        Object params = null;
        //属性设置
        if (ThingService.TYPE_PROPERTY.equals(type)) {
            List<ThingModel.Property> properties = model.getProperties();
            if (properties == null) {
                return;
            }
            params = parseProperties(properties, (Map<?, ?>) service.getParams());
        } else if (ThingService.TYPE_SERVICE.equals(type)) {
            //服务调用
            Map<String, ThingModel.Service> services = model.serviceMap();
            ThingModel.Service s = services.get(identifier);
            if (s == null) {
                return;
            }
            params = parseParams(s.getInputData(), (Map<?, ?>) service.getParams());
        }
        service.setParams(params);
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
