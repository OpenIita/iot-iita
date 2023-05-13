package cc.iotkit.data.model;

import cc.iotkit.model.screen.BigScreen;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：tfd
 * @Date：2023/5/6 15:46
 */
@Mapper
public interface BigScreenMapper {

    BigScreenMapper M = Mappers.getMapper(BigScreenMapper.class);

    BigScreen toDto(TbBigScreen vo);

    TbBigScreen toVo(BigScreen dto);

    static List<BigScreen> toDto(List<TbBigScreen> list) {
        return list.stream().map(M::toDto).collect(Collectors.toList());
    }
}
