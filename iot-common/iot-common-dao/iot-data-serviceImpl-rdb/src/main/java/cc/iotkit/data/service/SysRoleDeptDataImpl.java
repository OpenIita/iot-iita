package cc.iotkit.data.service;

import cc.iotkit.data.system.ISysRoleDeptData;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.system.SysRoleDept;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

import static cc.iotkit.data.model.QTbSysRoleDept.tbSysRoleDept;

/**
 * author: 石恒
 * date: 2023-05-30 16:20
 * description:
 **/
@Primary
@Service
@RequiredArgsConstructor
public class SysRoleDeptDataImpl implements ISysRoleDeptData {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void deleteByRoleId(List<Long> roleIds) {
        jpaQueryFactory.delete(tbSysRoleDept).where(PredicateBuilder.instance().and(tbSysRoleDept.roleId.in(roleIds)).build());
    }

    @Override
    public long insertBatch(List<SysRoleDept> list) {
        return 0;
    }
}
