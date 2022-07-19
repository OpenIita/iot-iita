package cc.iotkit.data.model;

import cc.iotkit.model.OauthClient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OauthClientMapper {

    OauthClientMapper M = Mappers.getMapper(OauthClientMapper.class);

    OauthClient toDto(TbOauthClient vo);

    TbOauthClient toVo(OauthClient dto);
}
