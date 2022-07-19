package cc.iotkit.data.model;

import cc.iotkit.model.protocol.ProtocolComponent;
import cc.iotkit.model.space.Space;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface SpaceMapper {

    SpaceMapper M = Mappers.getMapper(SpaceMapper.class);

    Space toDto(TbSpace vo);

    TbSpace toVo(Space dto);

    static List<Space> toDto(List<TbSpace> list) {
        return list.stream().map(M::toDto).collect(Collectors.toList());
    }

}
