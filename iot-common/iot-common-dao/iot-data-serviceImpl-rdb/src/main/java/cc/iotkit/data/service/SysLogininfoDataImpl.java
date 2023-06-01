package cc.iotkit.data.service;

import cc.iotkit.common.api.Paging;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.SysLogininforRepository;
import cc.iotkit.data.model.TbSysConfig;
import cc.iotkit.data.model.TbSysLogininfor;
import cc.iotkit.data.system.ISysLogininforData;
import cc.iotkit.data.util.PageBuilder;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.system.SysConfig;
import cc.iotkit.model.system.SysLogininfor;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static cc.iotkit.data.model.QTbSysLogininfor.tbSysLogininfor;

/**
 * @Author：tfd
 * @Date：2023/5/31 15:58
 */
@Primary
@Service
@RequiredArgsConstructor
public class SysLogininfoDataImpl implements ISysLogininforData, IJPACommData<SysLogininfor, Long> {

    private SysLogininforRepository logininfoRepository;


    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public JpaRepository getBaseRepository() {
        return logininfoRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbSysLogininfor.class;
    }

    @Override
    public List<SysLogininfor> findByConditions(SysLogininfor data) {
        List<TbSysLogininfor> ret=jpaQueryFactory.selectFrom(tbSysLogininfor).where(genPredicate(data))
                .orderBy(tbSysLogininfor.id.desc()).fetch();
        return MapstructUtils.convert(ret, SysLogininfor.class);
    }

    private static Predicate genPredicate(SysLogininfor data) {
        return PredicateBuilder.instance()
                .and(StringUtils.isNotBlank(data.getIpaddr()), () -> tbSysLogininfor.ipaddr.like(data.getIpaddr()))
                .and(StringUtils.isNotBlank(data.getStatus()), () -> tbSysLogininfor.status.eq(data.getStatus()))
                .and(StringUtils.isNotBlank(data.getUserName()), () -> tbSysLogininfor.userName.like(data.getUserName()))
                .build();
    }

    @Override
    public Paging<SysLogininfor> findByConditions(SysLogininfor cond, int page, int size) {
        Page<TbSysLogininfor> all = logininfoRepository.findAll(genPredicate(cond), PageBuilder.buildPageable(page, size));
        return PageBuilder.toPaging(all, SysConfig.class);
    }

    @Override
    public void deleteByTenantId(String tenantId) {

    }

    @Override
    public void deleteAll() {
        logininfoRepository.deleteAll();
    }



    @Override
    public void deleteByIds(Collection<Long> longs) {
        logininfoRepository.deleteAllByIdInBatch(longs);
    }
}
