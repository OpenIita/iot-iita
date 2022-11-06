package cc.iotkit.data.model;

import cc.iotkit.model.space.Home;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface HomeMapper {

    HomeMapper M = Mappers.getMapper(HomeMapper.class);

    Home toDto(TbHome vo);

    TbHome toVo(Home dto);

    static List<Home> toDto(List<TbHome> homes) {
        return homes.stream().map(M::toDto).collect(Collectors.toList());
    }
}
