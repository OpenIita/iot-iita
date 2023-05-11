package cc.iotkit.data.model;

import cc.iotkit.model.notify.Channel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * author: 石恒
 * date: 2023-05-11 18:04
 * description:
 **/
@Mapper
public interface ChannelMapper {

    ChannelMapper M = Mappers.getMapper(ChannelMapper.class);

    Channel toDto(TbChannel vo);

    TbChannel toVo(Channel dto);
}
