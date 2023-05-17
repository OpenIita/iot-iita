package cc.iotkit.data.convert.system;

import cc.iotkit.data.model.TbAlertConfig;
import cc.iotkit.model.alert.AlertConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface SysConfigMapper {

    SysConfigMapper M = Mappers.getMapper(SysConfigMapper.class);

    AlertConfig toDto(TbAlertConfig vo);

    TbAlertConfig toVo(AlertConfig dto);

    static List<AlertConfig> toDto(List<TbAlertConfig> alertConfigs) {
        return alertConfigs.stream().map(M::toDto).collect(Collectors.toList());
    }
}
