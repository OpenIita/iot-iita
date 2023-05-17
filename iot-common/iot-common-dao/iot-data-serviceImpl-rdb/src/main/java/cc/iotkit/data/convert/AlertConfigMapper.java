package cc.iotkit.data.convert;

import cc.iotkit.data.model.TbAlertConfig;
import cc.iotkit.model.alert.AlertConfig;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AlertConfigMapper {

    AlertConfigMapper M = Mappers.getMapper(AlertConfigMapper.class);

    AlertConfig toDto(TbAlertConfig vo);

    TbAlertConfig toVo(AlertConfig dto);

    static List<AlertConfig> toDto(List<TbAlertConfig> alertConfigs) {
        return alertConfigs.stream().map(M::toDto).collect(Collectors.toList());
    }
}
