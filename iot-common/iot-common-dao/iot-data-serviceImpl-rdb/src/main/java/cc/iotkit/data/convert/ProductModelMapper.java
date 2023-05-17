package cc.iotkit.data.convert;

import cc.iotkit.data.model.TbProductModel;
import cc.iotkit.model.product.ProductModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProductModelMapper {

    ProductModelMapper M = Mappers.getMapper(ProductModelMapper.class);

    ProductModel toDto(TbProductModel vo);

    TbProductModel toVo(ProductModel dto);

    static List<ProductModel> toDto(List<TbProductModel> list) {
        return list.stream().map(M::toDto).collect(Collectors.toList());
    }
}
