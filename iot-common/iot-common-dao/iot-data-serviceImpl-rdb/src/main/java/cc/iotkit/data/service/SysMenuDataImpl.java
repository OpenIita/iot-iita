package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.dao.SysMenuRepository;
import cc.iotkit.data.model.TbSysMenu;
import cc.iotkit.data.system.ISysMenuData;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.system.SysMenu;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static cc.iotkit.data.model.QTbSysMenu.tbSysMenu;
import static cc.iotkit.data.model.QTbSysRole.tbSysRole;
import static cc.iotkit.data.model.QTbSysRoleMenu.tbSysRoleMenu;
import static cc.iotkit.data.model.QTbSysUserRole.tbSysUserRole;


/**
 * @Author: 石恒
 * @Date: 2023/5/28 15:43
 * @Description:
 */
@Primary
@Service
@RequiredArgsConstructor
public class SysMenuDataImpl implements ISysMenuData {

    private final SysMenuRepository sysMenuRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public SysMenu findById(Long id) {
        TbSysMenu tbSysMenu = sysMenuRepository.findById(id).orElseThrow(() ->
                new BizException(ErrCode.DATA_NOT_EXIST));
        return MapstructUtils.convert(tbSysMenu, SysMenu.class);
    }

    @Override
    public List<SysMenu> findByIds(Collection<Long> ids) {
        List<TbSysMenu> list = sysMenuRepository.findAllById(ids);
        return MapstructUtils.convert(list, SysMenu.class);
    }

    @Override
    public SysMenu save(SysMenu data) {
        sysMenuRepository.save(MapstructUtils.convert(data, TbSysMenu.class));
        return data;
    }

    @Override
    public void batchSave(List<SysMenu> data) {
        List<TbSysMenu> tbSysMenus = data.stream().map(e -> MapstructUtils.convert(e, TbSysMenu.class)).collect(Collectors.toList());
        sysMenuRepository.saveAll(tbSysMenus);
    }

    @Override
    public void deleteById(Long id) {
        sysMenuRepository.deleteById(id);
    }

    @Override
    public void deleteByIds(Collection<Long> ids) {
        sysMenuRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<SysMenu> findAll() {
        return null;
    }

    @Override
    public Paging<SysMenu> findAll(PageRequest<SysMenu> pageRequest) {
        return null;
    }

    @Override
    public List<SysMenu> findAllByCondition(SysMenu data) {
        return null;
    }

    @Override
    public SysMenu findOneByCondition(SysMenu data) {
        return null;
    }

    @Override
    public List<SysMenu> findByUserId(Long userId) {
        return null;
    }

    @Override
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId, boolean isSuperAdmin) {

        PredicateBuilder predicateBuilder = PredicateBuilder.instance()
                .and(StringUtils.isNotBlank(menu.getMenuName()), () -> tbSysMenu.menuName.like(menu.getMenuName()))
                .and(StringUtils.isNotBlank(menu.getVisible()), () -> tbSysMenu.visible.eq(menu.getVisible()))
                .and(StringUtils.isNotBlank(menu.getStatus()), () -> tbSysMenu.status.eq(menu.getStatus()));

        // 管理员显示所有菜单信息
        List<TbSysMenu> tbSysMenuList;
        if (isSuperAdmin) {
            tbSysMenuList = jpaQueryFactory.select(tbSysMenu)
                    .where(predicateBuilder.build())
                    .orderBy(tbSysMenu.parentId.asc(), tbSysMenu.orderNum.asc()).fetch();
        } else {
            tbSysMenuList = jpaQueryFactory.select(Projections.bean(TbSysMenu.class, tbSysMenu.menuId.countDistinct().as(tbSysMenu.menuId),
                            tbSysMenu.parentId, tbSysMenu.menuName, tbSysMenu.path, tbSysMenu.component, tbSysMenu.queryParam,
                            tbSysMenu.visible, tbSysMenu.status, tbSysMenu.perms, tbSysMenu.isFrame, tbSysMenu.isCache, tbSysMenu.menuType,
                            tbSysMenu.icon, tbSysMenu.orderNum, tbSysMenu.createTime))
                    .from(tbSysMenu)
                    .leftJoin(tbSysRoleMenu).on(tbSysMenu.menuId.eq(tbSysRoleMenu.menuId))
                    .leftJoin(tbSysUserRole).on(tbSysRoleMenu.roleId.eq(tbSysUserRole.roleId))
                    .leftJoin(tbSysRole).on(tbSysUserRole.roleId.eq(tbSysRole.roleId))
                    .where(predicateBuilder
                            .and(tbSysUserRole.userId.eq(userId))
                            .build())
                    .orderBy(tbSysMenu.parentId.asc(), tbSysMenu.orderNum.asc()).fetch();

        }
        return MapstructUtils.convert(tbSysMenuList, SysMenu.class);

    }
}
