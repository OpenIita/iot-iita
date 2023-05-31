package cc.iotkit.data.service;

import cc.iotkit.common.api.Paging;
import cc.iotkit.common.constant.UserConstants;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.dao.SysDeptRepository;
import cc.iotkit.data.system.ISysDeptData;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.system.SysDept;
import cn.hutool.core.util.ObjectUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static cc.iotkit.data.model.QTbSysDept.tbSysDept;

/**
 * @Author：tfd
 * @Date：2023/5/30 13:43
 */
@Primary
@Service
@RequiredArgsConstructor
public class SysDeptDataImpl implements ISysDeptData {

    @Autowired
    private SysDeptRepository deptRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Paging<SysDept> findByConditions(Long parentId, String deptName, String status, int page, int size) {
        return null;
    }

    @Override
    public List<SysDept> findDepts(SysDept dept) {
        PredicateBuilder predicateBuilder = PredicateBuilder.instance()
                .and(tbSysDept.delFlag.eq(UserConstants.USER_NORMAL))
                .and(ObjectUtil.isNotNull(dept.getId()), () -> tbSysDept.id.eq(dept.getId()))
                .and(ObjectUtil.isNotNull(dept.getParentId()), () -> tbSysDept.parentId.eq(dept.getParentId()))
                .and(StringUtils.isNotEmpty(dept.getDeptName()), () -> tbSysDept.deptName.like(dept.getDeptName()))
                .and(StringUtils.isNotEmpty(dept.getStatus()), () -> tbSysDept.status.eq(dept.getStatus()));
        return MapstructUtils.convert(StreamSupport.stream(deptRepository.findAll(predicateBuilder.build()).spliterator(),false).collect(Collectors.toList()),SysDept.class);
    }

    @Override
    public List<SysDept> findByRoleId(Long roleId) {
        return null;
    }

    @Override
    public long countByParentId(Long parentId) {
        return 0;
    }

    @Override
    public List<SysDept> findByDeptId(Long deptId) {
        return MapstructUtils.convert(deptRepository.findAll().stream().filter(o->o.getAncestors().indexOf(deptId.toString())!=-1)
                .collect(Collectors.toList()),SysDept.class);
    }
}
