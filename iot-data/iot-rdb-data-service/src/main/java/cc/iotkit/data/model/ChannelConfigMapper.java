package cc.iotkit.data.model;

import cc.iotkit.model.notify.ChannelConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: 石恒
 * @Date: 2023/5/11 21:02
 * @Description:
 */
@Mapper
public interface ChannelConfigMapper {
    ChannelConfigMapper M = Mappers.getMapper(ChannelConfigMapper.class);

    ChannelConfig toDto(TbChannelConfig vo);

    TbChannelConfig toVo(ChannelConfig dto);
}
