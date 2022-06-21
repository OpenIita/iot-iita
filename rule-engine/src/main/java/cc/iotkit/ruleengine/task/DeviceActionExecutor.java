/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.ruleengine.task;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.dao.DeviceCache;
import cc.iotkit.ruleengine.action.DeviceAction;
import cc.iotkit.ruleengine.action.DeviceActionService;
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
    private DeviceCache deviceCache;
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
        DeviceAction action = actionMap.putIfAbsent(config.hashCode(), JsonUtil.parse(config, DeviceAction.class));
        if (action == null) {
            return;
        }

        log.info("start device service invoke,{}", JsonUtil.toJsonString(action));
        for (DeviceActionService.Service service : action.getServices()) {
            deviceActionService.invoke(service);
        }
    }
}
