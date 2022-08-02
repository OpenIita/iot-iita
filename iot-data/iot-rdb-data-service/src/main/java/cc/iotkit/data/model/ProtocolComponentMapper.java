package cc.iotkit.data.model;

import cc.iotkit.model.protocol.ProtocolComponent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProtocolComponentMapper {

    ProtocolComponentMapper M = Mappers.getMapper(ProtocolComponentMapper.class);

    ProtocolComponent toDto(TbProtocolComponent vo);

    TbProtocolComponent toVo(ProtocolComponent dto);

    static List<ProtocolComponent> toDto(List<TbProtocolComponent> list) {
        return list.stream().map(M::toDto).collect(Collectors.toList());
    }
}
