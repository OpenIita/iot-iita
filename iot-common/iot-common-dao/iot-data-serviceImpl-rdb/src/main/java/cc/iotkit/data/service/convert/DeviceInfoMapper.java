package cc.iotkit.data.service.convert;
/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */

import cc.iotkit.data.model.TbDeviceInfo;
import cc.iotkit.model.device.DeviceInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeviceInfoMapper {

    DeviceInfoMapper M = Mappers.getMapper(DeviceInfoMapper.class);

    @Mapping(target = "state", ignore = true)
    DeviceInfo toDto(TbDeviceInfo vo);

    @Mapping(target = "state", ignore = true)
    TbDeviceInfo toVo(DeviceInfo dto);
}
