package cc.iotkit.data.service;

import cc.iotkit.common.api.Paging;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.dao.SysLogininforRepository;
import cc.iotkit.data.model.TbSysLogininfor;
import cc.iotkit.data.system.ISysLogininforData;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.system.SysLogininfor;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
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
public class SysLogininfoDataImpl implements ISysLogininforData {

    private SysLogininforRepository logininfoRepository;


    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SysLogininfor> findByConditions(SysLogininfor data) {
        List<TbSysLogininfor> ret=jpaQueryFactory.selectFrom(tbSysLogininfor).where(PredicateBuilder.instance()
                .and(StringUtils.isNotBlank(data.getIpaddr()),()->tbSysLogininfor.ipaddr.like(data.getIpaddr()))
                .and(StringUtils.isNotBlank(data.getStatus()),()->tbSysLogininfor.status.eq(data.getStatus()))
                .and(StringUtils.isNotBlank(data.getUserName()),()->tbSysLogininfor.userName.like(data.getUserName()))
                .build())
                .orderBy(tbSysLogininfor.id.desc()).fetch();
        return MapstructUtils.convert(ret, SysLogininfor.class);
    }

    @Override
    public Paging<SysLogininfor> findByConditions(SysLogininfor cond, int page, int size) {
        return null;
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
