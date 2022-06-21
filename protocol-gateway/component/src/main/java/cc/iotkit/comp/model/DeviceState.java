/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comp.model;

import cc.iotkit.common.utils.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Data
@Slf4j
public class DeviceState {

    public static final String STATE_ONLINE = "online";
    public static final String STATE_OFFLINE = "offline";

    private String productKey;

    private String deviceName;

    private String state;

    private Parent parent;

    public static DeviceState from(Map map) {
        return JsonUtil.parse(JsonUtil.toJsonString(map), DeviceState.class);
    }

    @Data
    public static class Parent {
        private String productKey;
        private String deviceName;
    }
}
