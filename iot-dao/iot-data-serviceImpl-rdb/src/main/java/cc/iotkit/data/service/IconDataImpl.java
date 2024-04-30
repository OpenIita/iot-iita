package cc.iotkit.data.service;

import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.IconRepository;
import cc.iotkit.data.manager.IIconData;
import cc.iotkit.data.model.TbIcon;
import cc.iotkit.model.product.Icon;
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
public class IconDataImpl implements IIconData, IJPACommData<Icon, Long> {

    @Autowired
    private IconRepository iconRepository;

    @Override
    public JpaRepository getBaseRepository() {
        return iconRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbIcon.class;
    }

    @Override
    public Class getTClass() {
        return Icon.class;
    }
}
