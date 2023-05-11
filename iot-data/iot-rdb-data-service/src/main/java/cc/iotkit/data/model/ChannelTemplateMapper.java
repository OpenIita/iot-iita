package cc.iotkit.data.model;

import cc.iotkit.model.notify.ChannelTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: 石恒
 * @Date: 2023/5/11 21:02
 * @Description:
 */
@Mapper
public interface ChannelTemplateMapper {
    ChannelTemplateMapper M = Mappers.getMapper(ChannelTemplateMapper.class);

    ChannelTemplate toDto(TbChannelTemplate vo);

    TbChannelTemplate toVo(ChannelTemplate dto);
}
