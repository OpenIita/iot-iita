package cc.iotkit.data.service;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.model.QTbSysRoleMenu;
import cc.iotkit.data.model.TbSysRoleMenu;
import cc.iotkit.data.model.TbSysUserRole;
import cc.iotkit.data.system.ISysRoleMenuData;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.system.SysRoleMenu;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

import static cc.iotkit.data.model.QTbSysRoleMenu.tbSysRoleMenu;

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

    @Override
    public long insertBatch(List<SysRoleMenu> list) {
        return jpaQueryFactory.insert(tbSysRoleMenu).values(List.of(Objects.requireNonNull(MapstructUtils.convert(list, TbSysRoleMenu.class)))).execute();
    }

    @Override
    public long deleteByRoleId(List<Long> ids) {
        return jpaQueryFactory.delete(tbSysRoleMenu).where(tbSysRoleMenu.roleId.in(ids)).execute();
    }
}
