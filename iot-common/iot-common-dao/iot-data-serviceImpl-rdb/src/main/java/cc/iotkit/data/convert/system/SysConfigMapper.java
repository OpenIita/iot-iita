package cc.iotkit.data.convert.system;

import cc.iotkit.data.model.system.TbSysConfig;
import cc.iotkit.model.system.SysConfig;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SysConfigMapper {

    SysConfigMapper M = Mappers.getMapper(SysConfigMapper.class);

    SysConfig toDto(TbSysConfig vo);

    TbSysConfig toVo(SysConfig dto);

    static List<SysConfig> toDto(List<TbSysConfig> alertConfigs) {
        return alertConfigs.stream().map(M::toDto).collect(Collectors.toList());
    }
}
