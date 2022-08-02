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

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.model.product.ThingModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ThingModelMapper {

    ThingModelMapper M = Mappers.getMapper(ThingModelMapper.class);

    @Mapping(target = "model", ignore = true)
    ThingModel toDto(TbThingModel vo);

    static ThingModel toDtoFix(TbThingModel vo) {
        ThingModel dto = M.toDto(vo);
        dto.setModel(JsonUtil.parse(vo.getModel(), ThingModel.Model.class));
        return dto;
    }

    @Mapping(target = "model", ignore = true)
    TbThingModel toVo(ThingModel dto);

    static TbThingModel toVoFix(ThingModel dto) {
        TbThingModel vo = M.toVo(dto);
        vo.setModel(JsonUtil.toJsonString(dto.getModel()));
        return vo;
    }

    static List<ThingModel> toDto(List<TbThingModel> list) {
        return list.stream().map(ThingModelMapper::toDtoFix).collect(Collectors.toList());
    }

}
