package cc.iotkit.data.service;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.SysRoleRepository;
import cc.iotkit.data.system.ISysRoleData;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.system.SysRole;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

import static cc.iotkit.data.model.QTbSysMenu.tbSysMenu;
import static cc.iotkit.data.model.QTbSysRoleMenu.tbSysRoleMenu;

/**
 * author: 石恒
 * date: 2023-05-29 16:23
 * description:
 **/
@Primary
@Service
@RequiredArgsConstructor
public class SysRoleDataImpl implements ISysRoleData {

    private final SysRoleRepository sysRoleRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public SysRole findById(Long id) {
        return MapstructUtils.convert(sysRoleRepository.findById(id), SysRole.class);
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long roleId, boolean menuCheckStrictly) {

        List<Long> roleIds = jpaQueryFactory.select(Projections.bean(Long.class, tbSysMenu.menuId))
                .from(tbSysMenu)
                .innerJoin(tbSysRoleMenu).on(tbSysMenu.menuId.eq(tbSysRoleMenu.menuId))
                .where(PredicateBuilder.instance()
                        .and(tbSysRoleMenu.roleId.eq(roleId))
                        .build())
                .orderBy(tbSysMenu.parentId.asc(), tbSysMenu.orderNum.asc()).fetch();

        PredicateBuilder predicateBuilder = PredicateBuilder.instance()
                .and(tbSysRoleMenu.roleId.eq(roleId));

        if (menuCheckStrictly) {
            predicateBuilder.and(tbSysMenu.menuId.notIn(roleIds));
        }

        return jpaQueryFactory.select(Projections.bean(Long.class, tbSysMenu.menuId))
                .from(tbSysMenu)
                .leftJoin(tbSysRoleMenu).on(tbSysMenu.menuId.eq(tbSysRoleMenu.menuId))
                .where(predicateBuilder.build())
                .orderBy(tbSysMenu.parentId.asc(), tbSysMenu.orderNum.asc()).fetch();
    }
}
