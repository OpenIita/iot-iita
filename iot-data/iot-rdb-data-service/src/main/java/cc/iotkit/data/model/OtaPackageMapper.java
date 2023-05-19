package cc.iotkit.data.model;

import cc.iotkit.model.ota.OtaPackage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author: 石恒
 * @Date: 2023/5/19 21:32
 * @Description:
 */
@Mapper
public interface OtaPackageMapper {
    OtaPackageMapper M = Mappers.getMapper(OtaPackageMapper.class);

    OtaPackage toDto(TbOtaPackage vo);

    TbOtaPackage toVo(OtaPackage dto);
}
