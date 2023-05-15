package cc.iotkit.data.convert;

import cc.iotkit.data.model.TbProtocolConverter;
import cc.iotkit.model.protocol.ProtocolConverter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProtocolConverterMapper {

    ProtocolConverterMapper M = Mappers.getMapper(ProtocolConverterMapper.class);

    ProtocolConverter toDto(TbProtocolConverter vo);

    TbProtocolConverter toVo(ProtocolConverter dto);

    static List<ProtocolConverter> toDto(List<TbProtocolConverter> list) {
        return list.stream().map(M::toDto).collect(Collectors.toList());
    }
}
