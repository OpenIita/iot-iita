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
package cc.iotkit.model.product;

import cc.iotkit.model.Id;
import cc.iotkit.model.TenantModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sjg
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThingModel extends TenantModel implements Id<Long>, Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String productKey;

    private Model model;

    public ThingModel(String productKey) {
        this.productKey = productKey;
    }

    @Data
    public static class Model {
        private List<Property> properties;
        private List<Service> services;
        private List<Event> events;

        public Map<String, Service> serviceMap() {
            if (services == null) {
                return new HashMap<>();
            }
            return services.stream().collect(Collectors.toMap(Service::getIdentifier, s -> s));
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Property {
        private String identifier;
        private DataType dataType;
        private String name;
        private String accessMode = "rw";

        // 描述
        private String description;

        // 单位
        private String unit;
        // 图标id
        private Long iconId;
        // 图标信息
        private Icon icon;
        private String proData;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Parameter {
        private String identifier;
        private DataType dataType;
        private String name;
        private Boolean required = false;
    }

    @Data
    public static class Service {
        private String identifier;
        private List<Parameter> inputData;
        private List<Parameter> outputData;
        private String name;
    }

    @Data
    public static class Event {
        private String identifier;
        private List<Parameter> outputData;
        private String name;
    }

    @Data
    public static class DataType {
        private String type;
        private Object specs;

        public <T> Object parse(T value) {
            if (value == null) {
                return null;
            }

            String val = value.toString();
            type = type.toLowerCase();
            switch (type) {
                case "bool":
                case "enum":
                    return val;
                case "int":
                    return Integer.parseInt(val);
                default:
                    return val;
            }

        }
    }
}
