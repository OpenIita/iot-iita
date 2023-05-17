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

import cc.iotkit.data.model.TbUserInfo;
import cc.iotkit.model.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserInfoMapper {

    UserInfoMapper M = Mappers.getMapper(UserInfoMapper.class);

    @Mappings({
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "permissions", ignore = true),
            @Mapping(target = "usePlatforms", ignore = true)
    }
    )
    UserInfo toDto(TbUserInfo vo);

    static UserInfo toDtoFix(TbUserInfo vo) {
        UserInfo dto = M.toDto(vo);
        dto.setRoles(splitToList(vo.getRoles()));
        dto.setPermissions(splitToList(vo.getPermissions()));
        dto.setUsePlatforms(splitToList(vo.getUsePlatforms()));
        return dto;
    }

    @Mappings({
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "permissions", ignore = true),
            @Mapping(target = "usePlatforms", ignore = true)
    }
    )
    TbUserInfo toVo(UserInfo dto);

    static TbUserInfo toVoFix(UserInfo dto) {
        TbUserInfo vo = M.toVo(dto);
        vo.setRoles(listToStr(dto.getRoles()));
        vo.setPermissions(listToStr(dto.getPermissions()));
        vo.setUsePlatforms(listToStr(dto.getUsePlatforms()));
        return vo;
    }

    static List<UserInfo> toDto(List<TbUserInfo> list) {
        return list.stream().map(UserInfoMapper::toDtoFix).collect(Collectors.toList());
    }

    static List<String> splitToList(String str) {
        if (StringUtils.isBlank(str)) {
            return new ArrayList<>();
        }
        return Arrays.asList(str.split(","));
    }

    static String listToStr(List<String> list) {
        if (list == null || list.size() == 0) {
            return "";
        }
        return String.join(",", list);
    }

}
