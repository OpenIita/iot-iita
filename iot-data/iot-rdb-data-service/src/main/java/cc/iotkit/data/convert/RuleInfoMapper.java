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

import cc.iotkit.common.utils.JsonUtil;
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
        dto.setListeners(JsonUtil.parse(vo.getListeners(), new TypeReference<>() {
        }));
        dto.setFilters(JsonUtil.parse(vo.getFilters(), new TypeReference<>() {
        }));
        dto.setActions(JsonUtil.parse(vo.getActions(), new TypeReference<>() {
        }));
        return dto;
    }

    static TbRuleInfo toVoFix(RuleInfo dto) {
        TbRuleInfo vo = M.toVo(dto);
        vo.setListeners(JsonUtil.toJsonString(dto.getListeners()));
        vo.setFilters(JsonUtil.toJsonString(dto.getFilters()));
        vo.setActions(JsonUtil.toJsonString(dto.getActions()));
        return vo;
    }

    static List<RuleInfo> toDto(List<TbRuleInfo> list) {
        return list.stream().map(RuleInfoMapper::toDtoFix).collect(Collectors.toList());
    }
}
