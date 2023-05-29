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
import cc.iotkit.data.model.TbTaskInfo;
import cc.iotkit.model.rule.TaskInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface TaskInfoMapper {

    TaskInfoMapper M = Mappers.getMapper(TaskInfoMapper.class);

    @Mapping(target = "actions", ignore = true)
    TaskInfo toDto(TbTaskInfo vo);

    static TaskInfo toDtoFix(TbTaskInfo vo) {
        TaskInfo dto = M.toDto(vo);
        dto.setActions(JsonUtils.parseObject(vo.getActions(), new TypeReference<>() {
        }));
        return dto;
    }

    @Mapping(target = "actions", ignore = true)
    TbTaskInfo toVo(TaskInfo dto);

    static TbTaskInfo toVoFix(TaskInfo dto) {
        TbTaskInfo vo = M.toVo(dto);
        vo.setActions(JsonUtils.toJsonString(dto.getActions()));
        return vo;
    }

    static List<TaskInfo> toDto(List<TbTaskInfo> list) {
        return list.stream().map(TaskInfoMapper::toDtoFix).collect(Collectors.toList());
    }

}
