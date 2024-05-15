/*
 *
 *  * | Licensed 未经许可不能去掉「OPENIITA」相关版权
 *  * +----------------------------------------------------------------------
 *  * | Author: xw2sy@163.com
 *  * +----------------------------------------------------------------------
 *
 *  Copyright [2024] [OPENIITA]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */
package cc.iotkit.manager.service;

import cc.iotkit.common.thing.ThingModelMessage;
import cc.iotkit.common.thing.ThingService;
import cc.iotkit.data.manager.IThingModelData;
import cc.iotkit.model.product.ThingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ThingModelService {
    @Autowired
    private IThingModelData thingModelData;

    public void parseParams(ThingService<Object> service) {
        ThingModel thingModel = thingModelData.findByProductKey(service.getProductKey());
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
            if(identifier.equals(ThingModelMessage.ID_PROPERTY_GET)){
                params = service.getParams();
            }
            else {
                params = parseProperties(properties, (Map<?, ?>) service.getParams());
            }
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
