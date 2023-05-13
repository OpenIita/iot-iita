package cc.iotkit.data.model;

import cc.iotkit.model.screen.BigScreenApi;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：tfd
 * @Date：2023/5/6 15:46
 */
@Mapper
public interface BigScreenApiMapper {

    BigScreenApiMapper M = Mappers.getMapper(BigScreenApiMapper.class);

    BigScreenApi toDto(TbBigScreenApi vo);

    TbBigScreenApi toVo(BigScreenApi dto);

    static List<BigScreenApi> toDto(List<TbBigScreenApi> list) {
        return list.stream().map(M::toDto).collect(Collectors.toList());
    }
}
