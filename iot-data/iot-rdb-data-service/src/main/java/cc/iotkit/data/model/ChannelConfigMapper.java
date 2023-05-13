package cc.iotkit.data.model;

import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.model.notify.ChannelConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

/**
 * @Author: 石恒
 * @Date: 2023/5/11 21:02
 * @Description:
 */
@Mapper
public interface ChannelConfigMapper {
    ChannelConfigMapper M = Mappers.getMapper(ChannelConfigMapper.class);

    @Mappings({
            @Mapping(target = "param", expression = "java(stingToParam(vo.getParam()))")
    })
    ChannelConfig toDto(TbChannelConfig vo);

    @Mappings({
            @Mapping(target = "param", expression = "java(paramToSting(dto.getParam()))")
    })
    TbChannelConfig toVo(ChannelConfig dto);

    default String paramToSting(ChannelConfig.ChannelParam param){
        if (Objects.isNull(param)) {
            return null;
        }
        return JsonUtil.toJsonString(param);

    }
    default ChannelConfig.ChannelParam stingToParam(String param){
        if (Objects.isNull(param)) {
            return null;
        }
        return JsonUtil.parse(param,ChannelConfig.ChannelParam.class);

    }
}
