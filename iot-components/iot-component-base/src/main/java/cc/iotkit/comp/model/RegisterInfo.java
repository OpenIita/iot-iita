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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 注册信息
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants
public class RegisterInfo {

    private String productKey;

    private String deviceName;

    private String model;

    private Map<String, Object> tag;

    private List<SubDevice> subDevices;

    public RegisterInfo(String productKey, String deviceName, String model) {
        this.productKey = productKey;
        this.deviceName = deviceName;
        this.model = model;
    }

    public RegisterInfo(String productKey, String deviceName, String subProductKey, String subDeviceName) {
        this.productKey = productKey;
        this.deviceName = deviceName;
        if (subProductKey != null && subDeviceName != null) {
            SubDevice subDevice = new SubDevice(subProductKey, subDeviceName, null, null);
            subDevices = new ArrayList<>();
            subDevices.add(subDevice);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldNameConstants
    public static class SubDevice {

        private String productKey;

        private String deviceName;

        private String model;

        private Map<String, Object> tag;
    }
}