package cc.iotkit.ruleengine.action;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.dao.DeviceCache;
import cc.iotkit.deviceapi.IDeviceService;
import cc.iotkit.deviceapi.Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 设备输出器
 */
@Slf4j
@Component
public class DeviceActionExecutor implements ActionExecutor<DeviceAction> {

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private DeviceCache deviceCache;

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
        DeviceAction action = JsonUtil.parse(config, DeviceAction.class);
        log.info("start device service invoke,{}", JsonUtil.toJsonString(action));
        for (Service service : action.getServices()) {
            deviceService.invoke(service);
        }
    }
}
