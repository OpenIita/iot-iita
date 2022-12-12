/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.comp.mqtt;

import lombok.Data;

@Data
public class TransparentMsg {

    private String productKey;

    /**
     * 生成给设备端的消息id
     */
    private String mid;

    private String model;

    private String deviceName;

    private String data;

}
