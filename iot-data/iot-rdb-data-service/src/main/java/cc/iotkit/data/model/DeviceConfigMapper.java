/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.data.model;

import cc.iotkit.model.device.DeviceConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeviceConfigMapper {

    DeviceConfigMapper M = Mappers.getMapper(DeviceConfigMapper.class);

    DeviceConfig toDto(TbDeviceConfig vo);

    TbDeviceConfig toVo(DeviceConfig dto);
}
