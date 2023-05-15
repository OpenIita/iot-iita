package cc.iotkit.data.convert;

import cc.iotkit.data.model.TbAlertRecord;
import cc.iotkit.model.alert.AlertRecord;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AlertRecordMapper {

    AlertRecordMapper M = Mappers.getMapper(AlertRecordMapper.class);

    AlertRecord toDto(TbAlertRecord vo);

    TbAlertRecord toVo(AlertRecord dto);

    static List<AlertRecord> toDto(List<TbAlertRecord> alertRecords) {
        return alertRecords.stream().map(M::toDto).collect(Collectors.toList());
    }
}
