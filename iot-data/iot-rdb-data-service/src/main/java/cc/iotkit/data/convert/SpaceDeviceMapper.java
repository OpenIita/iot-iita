/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.data.convert;

import cc.iotkit.data.model.TbSpaceDevice;
import cc.iotkit.model.space.SpaceDevice;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface SpaceDeviceMapper {

    SpaceDeviceMapper M = Mappers.getMapper(SpaceDeviceMapper.class);

    SpaceDevice toDto(TbSpaceDevice vo);

    TbSpaceDevice toVo(SpaceDevice dto);

    static List<SpaceDevice> toDto(List<TbSpaceDevice> list) {
        return list.stream().map(M::toDto).collect(Collectors.toList());
    }

}
