package cc.iotkit.data.service;

import cc.iotkit.data.model.QTbSysRoleMenu;
import cc.iotkit.data.model.TbSysRoleMenu;
import cc.iotkit.data.system.ISysRoleMenuData;
import cc.iotkit.data.util.PredicateBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * author: 石恒
 * date: 2023-05-30 11:00
 * description:
 **/
@Primary
@Service
@RequiredArgsConstructor
public class SysRoleMenuDataImpl implements ISysRoleMenuData {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean checkMenuExistRole(Long menuId) {
        TbSysRoleMenu tbSysRoleMenu = jpaQueryFactory
                .select(QTbSysRoleMenu.tbSysRoleMenu)
                .from(QTbSysRoleMenu.tbSysRoleMenu)
                .where(PredicateBuilder.instance()
                        .and(QTbSysRoleMenu.tbSysRoleMenu.menuId.eq(menuId))
                        .build()).fetchOne();
        return Objects.nonNull(tbSysRoleMenu);
    }
}
