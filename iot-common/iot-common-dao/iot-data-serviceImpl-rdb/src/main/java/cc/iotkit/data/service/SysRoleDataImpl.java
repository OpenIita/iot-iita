package cc.iotkit.data.service;

import cc.iotkit.common.constant.UserConstants;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.SysRoleRepository;
import cc.iotkit.data.model.TbSysRole;
import cc.iotkit.data.system.ISysRoleData;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.system.SysRole;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static cc.iotkit.data.model.QTbSysDept.tbSysDept;
import static cc.iotkit.data.model.QTbSysMenu.tbSysMenu;
import static cc.iotkit.data.model.QTbSysRole.tbSysRole;
import static cc.iotkit.data.model.QTbSysRoleMenu.tbSysRoleMenu;
import static cc.iotkit.data.model.QTbSysUser.tbSysUser;
import static cc.iotkit.data.model.QTbSysUserRole.tbSysUserRole;

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

    @Override
    public List<SysRole> selectRolePermissionByUserId(Long userId) {
        return buildQueryTitle(PredicateBuilder.instance()
                .and(tbSysRole.delFlag.eq("0"))
                .and(tbSysUserRole.userId.eq(userId))
                .build());
    }

    @Override
    public List<Long> selectRoleListByUserId(Long userId) {
        return jpaQueryFactory.select(tbSysRole.id).from(tbSysRole)
                .leftJoin(tbSysUserRole).on(tbSysUserRole.roleId.eq(tbSysRole.id))
                .leftJoin(tbSysUser).on(tbSysUser.id.eq(tbSysUserRole.userId))
                .where(PredicateBuilder.instance().and(tbSysUser.id.eq(userId)).build()).fetch();

    }

    @Override
    public boolean checkRoleNameUnique(SysRole role) {
        final TbSysRole tbSysRoleRes = jpaQueryFactory.select(tbSysRole).from(tbSysRole)
                .where(PredicateBuilder.instance()
                        .and(tbSysRole.roleName.eq(role.getRoleName()))
                        .and(Objects.nonNull(role.getId()), () -> tbSysRole.id.eq(role.getId()))
                        .build()).fetchOne();
        return Objects.isNull(tbSysRoleRes);
    }

    @Override
    public boolean checkRoleKeyUnique(SysRole role) {
        final TbSysRole tbSysRoleRes = jpaQueryFactory.select(tbSysRole).from(tbSysRole)
                .where(PredicateBuilder.instance()
                        .and(tbSysRole.roleKey.eq(role.getRoleKey()))
                        .and(Objects.nonNull(role.getId()), () -> tbSysRole.id.eq(role.getId()))
                        .build()).fetchOne();
        return Objects.isNull(tbSysRoleRes);
    }

    @Override
    public int updateById(SysRole role) {
        long execute = jpaQueryFactory.update(tbSysRole)
                .where(PredicateBuilder.instance().and(tbSysRole.id.eq(role.getId())).build()).execute();
        return Integer.parseInt(execute + "");
    }

    @Override
    public List<SysRole> selectRoleList(SysRole role) {
        return buildQueryTitle(buildQueryWrapper(role));
    }

    private List<SysRole> buildQueryTitle(Predicate predicate) {
        return jpaQueryFactory.select(Projections.bean(SysRole.class, tbSysRole.id.countDistinct(), tbSysRole.roleName,
                        tbSysRole.roleKey, tbSysRole.roleSort, tbSysRole.menuCheckStrictly, tbSysRole.deptCheckStrictly,
                        tbSysRole.status, tbSysRole.delFlag, tbSysRole.createTime, tbSysRole.remark))
                .from(tbSysRole)
                .leftJoin(tbSysUserRole).on(tbSysUserRole.roleId.eq(tbSysRole.id))
                .leftJoin(tbSysUser).on(tbSysUser.id.eq(tbSysUserRole.userId))
                .leftJoin(tbSysDept).on(tbSysUser.deptId.eq(tbSysDept.id))
                .where(predicate)
                .orderBy(tbSysRole.roleSort.asc(), tbSysRole.createTime.asc()).fetch();
    }

    private Predicate buildQueryWrapper(SysRole role) {
        return PredicateBuilder.instance()
                .and(tbSysRole.delFlag.eq(UserConstants.ROLE_NORMAL))
                .and(Objects.nonNull(role.getId()), () -> tbSysRole.id.eq(role.getId()))
                .and(StringUtils.isNotBlank(role.getRoleName()), () -> tbSysRole.roleName.like(role.getRoleName()))
                .and(StringUtils.isNotBlank(role.getStatus()), () -> tbSysRole.roleName.eq(role.getStatus()))
                .and(StringUtils.isNotBlank(role.getRoleKey()), () -> tbSysRole.roleKey.like(role.getRoleKey()))
                .build();

    }

    @Override
    public void deleteById(Long id) {
        jpaQueryFactory.delete(tbSysRole).where(tbSysRole.id.eq(id)).execute();
    }
}
