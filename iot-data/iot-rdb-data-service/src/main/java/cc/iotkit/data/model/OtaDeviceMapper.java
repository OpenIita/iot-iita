package cc.iotkit.data.model;

import cc.iotkit.model.ota.OtaDevice;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: 石恒
 * @Date: 2023/5/25 23:39
 * @Description:
 */
@Mapper
public interface OtaDeviceMapper {
    OtaDeviceMapper M = Mappers.getMapper(OtaDeviceMapper.class);

    OtaDevice toDto(TbOtaDevice vo);

    TbOtaDevice toVo(OtaDevice dto);
}
