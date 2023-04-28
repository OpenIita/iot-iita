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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants
public class DeviceState {

    public static final String STATE_ONLINE = "online";
    public static final String STATE_OFFLINE = "offline";

    private String productKey;

    private String deviceName;

    private String state;

    private Parent parent;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldNameConstants
    public static class Parent {
        private String productKey;
        private String deviceName;
    }
}
