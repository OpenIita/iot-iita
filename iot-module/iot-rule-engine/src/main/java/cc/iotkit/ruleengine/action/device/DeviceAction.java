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
package cc.iotkit.ruleengine.action.device;

import cc.iotkit.common.thing.ThingModelMessage;
import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.ruleengine.action.Action;
import cc.iotkit.ruleengine.action.device.DeviceActionService.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeviceAction implements Action<Service> {

    public static final String TYPE = "device";

    private String type;

    private List<DeviceActionService.Service> services;

    private DeviceActionService deviceActionService;

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public List<String> execute(ThingModelMessage msg) {
        List<String> results = new ArrayList<>();
        for (DeviceActionService.Service service : services) {
            deviceActionService.invoke(service);
            results.add(JsonUtils.toJsonString(service));
        }
        return results;
    }

}
