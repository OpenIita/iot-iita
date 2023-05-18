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
import cc.iotkit.data.model.TbVirtualDevice;
import cc.iotkit.model.device.VirtualDevice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface VirtualDeviceMapper {

    VirtualDeviceMapper M = Mappers.getMapper(VirtualDeviceMapper.class);

    @Mapping(target = "devices", ignore = true)
    VirtualDevice toDto(TbVirtualDevice vo);

    TbVirtualDevice toVo(VirtualDevice dto);

    static List<VirtualDevice> toDto(List<TbVirtualDevice> list) {
        return list.stream().map(VirtualDeviceMapper.M::toDto).collect(Collectors.toList());
    }

}
