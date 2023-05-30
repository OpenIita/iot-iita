package cc.iotkit.data.service;

import cc.iotkit.data.system.ISysRoleDeptData;
import cc.iotkit.data.util.PredicateBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

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
    public void delete(Long roleId) {
        jpaQueryFactory.delete(tbSysRoleDept).where(PredicateBuilder.instance().and(tbSysRoleDept.roleId.eq(roleId)).build());
    }
}
