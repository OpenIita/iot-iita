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

import cc.iotkit.data.model.TbSpace;
import cc.iotkit.model.space.Space;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface SpaceMapper {

    SpaceMapper M = Mappers.getMapper(SpaceMapper.class);

    Space toDto(TbSpace vo);

    TbSpace toVo(Space dto);

    static List<Space> toDto(List<TbSpace> list) {
        return list.stream().map(M::toDto).collect(Collectors.toList());
    }

}
