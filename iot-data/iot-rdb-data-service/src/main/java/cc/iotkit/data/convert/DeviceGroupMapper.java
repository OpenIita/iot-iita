package cc.iotkit.data.convert;

import cc.iotkit.data.model.TbDeviceGroup;
import cc.iotkit.model.device.DeviceGroup;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeviceGroupMapper {

    DeviceGroupMapper M = Mappers.getMapper(DeviceGroupMapper.class);

    DeviceGroup toDto(TbDeviceGroup vo);

    TbDeviceGroup toVo(DeviceGroup dto);
}
