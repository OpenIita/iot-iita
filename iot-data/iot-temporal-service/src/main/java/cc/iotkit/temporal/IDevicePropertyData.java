/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.temporal;


import cc.iotkit.model.device.message.DeviceProperty;

import java.util.List;

/**
 * 设备属性时序数据接口
 */
public interface IDevicePropertyData {

    /**
     * 按时间范围取设备指定属性的历史数据
     *
     * @param deviceId 设备id
     * @param name     属性名称
     * @param start    开始时间戳
     * @param end      结束时间戳
     */
    List<DeviceProperty> findDevicePropertyHistory(String deviceId, String name, long start, long end);

    /**
     * 添加多个属性
     *
     * @param properties 属性列表
     */
    void addProperties(List<DeviceProperty> properties);

}
