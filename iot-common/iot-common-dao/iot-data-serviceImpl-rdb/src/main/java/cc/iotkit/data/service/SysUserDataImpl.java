package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.constant.UserConstants;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.StreamUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.SysUserRepository;
import cc.iotkit.data.model.TbSysUser;
import cc.iotkit.data.system.ISysDeptData;
import cc.iotkit.data.system.ISysUserData;
import cc.iotkit.data.util.PageBuilder;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.system.SysDept;
import cc.iotkit.model.system.SysUser;
import cn.hutool.core.util.ObjectUtil;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static cc.iotkit.data.model.QTbSysDept.tbSysDept;
import static cc.iotkit.data.model.QTbSysUser.tbSysUser;


/**
 * @Author：tfd
 * @Date：2023/5/29 16:00
 */
@Primary
@Service
@RequiredArgsConstructor
public class SysUserDataImpl implements ISysUserData, IJPACommData<SysUser, Long> {

    private final SysUserRepository userRepository;

    private final ISysDeptData sysDeptData;

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public JpaRepository getBaseRepository() {
        return userRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbSysUser.class;
    }


    @Override
    public long countByDeptId(Long aLong) {
        return 0;
    }

    @Override
    public boolean checkUserNameUnique(SysUser user) {
        final TbSysUser ret = jpaQueryFactory.select(tbSysUser).from(tbSysUser)
                .where(PredicateBuilder.instance()
                        .and(tbSysUser.userName.eq(user.getUserName()))
                        .and(Objects.nonNull(user.getId()), () -> tbSysUser.id.eq(user.getId()))
                        .build()).fetchOne();
        return Objects.isNull(ret);
    }

    @Override
    public boolean checkPhoneUnique(SysUser user) {
        final TbSysUser ret = jpaQueryFactory.select(tbSysUser).from(tbSysUser)
                .where(PredicateBuilder.instance()
                        .and(tbSysUser.phonenumber.eq(user.getPhonenumber()))
                        .and(Objects.nonNull(user.getId()), () -> tbSysUser.id.eq(user.getId()))
                        .build()).fetchOne();
        return Objects.isNull(ret);
    }

    @Override
    public boolean checkEmailUnique(SysUser user) {
        final TbSysUser ret = jpaQueryFactory.select(tbSysUser).from(tbSysUser)
                .where(PredicateBuilder.instance()
                        .and(tbSysUser.email.eq(user.getEmail()))
                        .and(Objects.nonNull(user.getId()), () -> tbSysUser.id.eq(user.getId()))
                        .build()).fetchOne();
        return Objects.isNull(ret);
    }

    @Override
    public Paging<SysUser> findAll(PageRequest<SysUser> pageRequest) {
        return PageBuilder.toPaging(userRepository.findAll(buildQueryCondition(pageRequest.getData()), PageBuilder.toPageable(pageRequest)));
    }

    @Override
    public List<SysUser> findAllByCondition(SysUser user) {
        return buildQuery(buildQueryCondition(user));
    }

    private List<SysUser> buildQuery(Predicate predicate) {
        List<TbSysUser> users = jpaQueryFactory.select(Projections.bean(TbSysUser.class, tbSysUser.id, tbSysUser.deptId,
                tbSysUser.nickName, tbSysUser.userName, tbSysUser.email, tbSysUser.avatar, tbSysUser.phonenumber, tbSysUser.sex,
                tbSysUser.status, tbSysUser.delFlag, tbSysUser.loginIp, tbSysUser.loginDate, tbSysUser.createBy, tbSysUser.createTime,
                tbSysUser.remark, tbSysDept.deptName, tbSysDept.leader))
                .from(tbSysUser)
                .leftJoin(tbSysDept).on(tbSysUser.deptId.eq(tbSysDept.id))
                .where(predicate).fetch();
        return MapstructUtils.convert(users, SysUser.class);
    }

    private Predicate buildQueryCondition(SysUser user) {
        List<SysDept> depts = sysDeptData.findByDeptId(user.getDeptId());
        List<Long> ids = StreamUtils.toList(depts, SysDept::getId);
        ids.add(user.getDeptId());
        return PredicateBuilder.instance()
                .and(tbSysUser.delFlag.eq(UserConstants.USER_NORMAL))
                .and(ObjectUtil.isNotNull(user.getId()), () -> tbSysUser.id.eq(user.getId()))
                .and(StringUtils.isNotEmpty(user.getUserName()), () -> tbSysUser.userName.like(user.getUserName()))
                .and(StringUtils.isNotEmpty(user.getStatus()), () -> tbSysUser.status.eq(user.getStatus()))
                .and(StringUtils.isNotEmpty(user.getPhonenumber()), () -> tbSysUser.phonenumber.like(user.getPhonenumber()))
                .and(ObjectUtil.isNotNull(user.getDeptId()), () -> tbSysUser.deptId.in(ids)).build();
    }
}
