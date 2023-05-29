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
import cc.iotkit.data.model.TbRuleInfo;
import cc.iotkit.model.rule.RuleInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface RuleInfoMapper {

    RuleInfoMapper M = Mappers.getMapper(RuleInfoMapper.class);

    @Mappings({
            @Mapping(target = "listeners", ignore = true),
            @Mapping(target = "filters", ignore = true),
            @Mapping(target = "actions", ignore = true)
    }
    )
    RuleInfo toDto(TbRuleInfo vo);

    @Mappings({
            @Mapping(target = "listeners", ignore = true),
            @Mapping(target = "filters", ignore = true),
            @Mapping(target = "actions", ignore = true)
    }
    )
    TbRuleInfo toVo(RuleInfo dto);

    static RuleInfo toDtoFix(TbRuleInfo vo) {
        RuleInfo dto = M.toDto(vo);
        dto.setListeners(JsonUtils.parseObject(vo.getListeners(), new TypeReference<>() {
        }));
        dto.setFilters(JsonUtils.parseObject(vo.getFilters(), new TypeReference<>() {
        }));
        dto.setActions(JsonUtils.parseObject(vo.getActions(), new TypeReference<>() {
        }));
        return dto;
    }

    static TbRuleInfo toVoFix(RuleInfo dto) {
        TbRuleInfo vo = M.toVo(dto);
        vo.setListeners(JsonUtils.toJsonString(dto.getListeners()));
        vo.setFilters(JsonUtils.toJsonString(dto.getFilters()));
        vo.setActions(JsonUtils.toJsonString(dto.getActions()));
        return vo;
    }

    static List<RuleInfo> toDto(List<TbRuleInfo> list) {
        return list.stream().map(RuleInfoMapper::toDtoFix).collect(Collectors.toList());
    }
}
