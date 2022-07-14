package cc.iotkit.data.model;

import cc.iotkit.model.device.DeviceConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeviceConfigMapper {

    DeviceConfigMapper M = Mappers.getMapper(DeviceConfigMapper.class);

    DeviceConfig toDto(TbDeviceConfig vo);

    TbDeviceConfig toVo(DeviceConfig dto);
}
