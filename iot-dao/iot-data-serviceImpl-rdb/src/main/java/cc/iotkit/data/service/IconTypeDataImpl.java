package cc.iotkit.data.service;

import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.IconTypeRepository;
import cc.iotkit.data.manager.IIconTypeData;
import cc.iotkit.data.model.TbIconType;
import cc.iotkit.model.product.IconType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @Author：tfd
 * @Date：2024/4/28 16:42
 */
@Primary
@Service
public class IconTypeDataImpl implements IIconTypeData, IJPACommData<IconType, Long> {

    @Autowired
    private IconTypeRepository iconTypeRepository;

    @Override
    public JpaRepository getBaseRepository() {
        return iconTypeRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbIconType.class;
    }

    @Override
    public Class getTClass() {
        return IconType.class;
    }
}
