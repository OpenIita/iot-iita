/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.temporal.es.document;

import cc.iotkit.model.device.message.DeviceProperty;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DevicePropertyMapper {
    DevicePropertyMapper M = Mappers.getMapper(DevicePropertyMapper.class);

    DeviceProperty toDto(DocDeviceProperty vo);

    DocDeviceProperty toVo(DeviceProperty dto);
}
