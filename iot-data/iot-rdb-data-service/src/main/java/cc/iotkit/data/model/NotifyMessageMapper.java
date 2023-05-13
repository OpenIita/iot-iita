package cc.iotkit.data.model;

import cc.iotkit.model.notify.NotifyMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: 石恒
 * @Date: 2023/5/13 18:38
 * @Description:
 */
@Mapper
public interface NotifyMessageMapper {

    NotifyMessageMapper M = Mappers.getMapper(NotifyMessageMapper.class);

    NotifyMessage toDto(TbNotifyMessage vo);

    TbNotifyMessage toVo(NotifyMessage dto);

}
