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
package cc.iotkit.ruleengine.task;

import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.ruleengine.action.device.DeviceAction;
import cc.iotkit.ruleengine.action.device.DeviceActionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 设备输出器
 */
@Slf4j
@Component
public class DeviceActionExecutor implements ActionExecutor<DeviceAction> {

    @Autowired
    private DeviceActionService deviceActionService;

    private Map<Integer, DeviceAction> actionMap = new ConcurrentHashMap<>();

    @Override
    public String getName() {
        return "device";
    }

    @Override
    public void execute(String config) {
        if (StringUtils.isBlank(config)) {
            log.error("device executor's config is blank");
            return;
        }
        //将执行的数据转换为动作配置
        Integer code = config.hashCode();
        DeviceAction action = actionMap.computeIfAbsent(code, k -> JsonUtils.parseObject(config, DeviceAction.class));

        log.info("start device service invoke,{}", JsonUtils.toJsonString(action));
        for (DeviceActionService.Service service : action.getServices()) {
            deviceActionService.invoke(service);
        }
    }
}
