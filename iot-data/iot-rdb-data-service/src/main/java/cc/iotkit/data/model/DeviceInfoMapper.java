package cc.iotkit.data.model;

import cc.iotkit.model.device.DeviceInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeviceInfoMapper {

    DeviceInfoMapper M = Mappers.getMapper(DeviceInfoMapper.class);

    @Mapping(target = "state", ignore = true)
    DeviceInfo toDto(TbDeviceInfo vo);

    @Mapping(target = "state", ignore = true)
    TbDeviceInfo toVo(DeviceInfo dto);
}
