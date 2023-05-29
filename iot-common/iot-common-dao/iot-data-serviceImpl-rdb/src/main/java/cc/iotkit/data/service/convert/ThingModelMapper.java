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
import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.data.model.TbThingModel;
import cc.iotkit.model.product.ThingModel;
import org.apache.commons.lang3.StringUtils;
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
        if (vo == null) {
            return null;
        }
        ThingModel dto = M.toDto(vo);
        if (StringUtils.isNotBlank(vo.getModel())) {
            dto.setModel(JsonUtils.parseObject(vo.getModel(), ThingModel.Model.class));
        }
        return dto;
    }

    @Mapping(target = "model", ignore = true)
    TbThingModel toVo(ThingModel dto);

    static TbThingModel toVoFix(ThingModel dto) {
        TbThingModel vo = M.toVo(dto);
        vo.setModel(JsonUtils.toJsonString(dto.getModel()));
        return vo;
    }

    static List<ThingModel> toDto(List<TbThingModel> list) {
        return list.stream().map(ThingModelMapper::toDtoFix).collect(Collectors.toList());
    }

}
