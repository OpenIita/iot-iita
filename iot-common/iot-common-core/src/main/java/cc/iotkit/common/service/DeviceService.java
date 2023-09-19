package cc.iotkit.common.service;

import cc.iotkit.common.thing.ThingService;

/**
 * 通用设备服务
 *
 * @author sjg
 */
public interface DeviceService {

    /**
     * 调用设备服务
     *
     * @param service 服务
     */
    void invoke(ThingService<?> service);

}
