package cc.iotkit.data.convert;

import cc.iotkit.data.model.TbProduct;
import cc.iotkit.model.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProductMapper {

    ProductMapper M = Mappers.getMapper(ProductMapper.class);

    Product toDto(TbProduct vo);

    TbProduct toVo(Product dto);

    static List<Product> toDto(List<TbProduct> products) {
        return products.stream().map(M::toDto).collect(Collectors.toList());
    }
}
