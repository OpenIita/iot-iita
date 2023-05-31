package cc.iotkit.data.service;

import cc.iotkit.common.api.Paging;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.dao.SysOperLogRepository;
import cc.iotkit.data.model.TbSysOperLog;
import cc.iotkit.data.system.ISysOperLogData;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.system.SysOperLog;
import cn.hutool.core.util.ArrayUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static cc.iotkit.data.model.QTbSysOperLog.tbSysOperLog;

/**
 * @Author：tfd
 * @Date：2023/5/31 15:24
 */
@Primary
@Service
@RequiredArgsConstructor
public class SysOperLogDataImpl implements ISysOperLogData {

    private SysOperLogRepository operLogRepository;


    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Paging<SysOperLog> findByConditions(String tenantId, String title, Integer businessType, Integer status, int page, int size) {
        return null;
    }

    @Override
    public void deleteByTenantId(String tenantId) {

    }

    @Override
    public void deleteAll() {
        operLogRepository.deleteAll();
    }

    @Override
    public void deleteByIds(Collection<Long> longs) {
        operLogRepository.deleteAllByIdInBatch(longs);
    }

    @Override
    public List<SysOperLog> findAllByCondition(SysOperLog data) {
        List<TbSysOperLog> ret=jpaQueryFactory.selectFrom(tbSysOperLog).where(PredicateBuilder.instance()
                .and(StringUtils.isNotBlank(data.getTitle()),()->tbSysOperLog.title.like(data.getTitle()))
                .and(data.getBusinessType()!=null&&data.getBusinessType()>0,()->tbSysOperLog.businessType.eq(data.getBusinessType()))
                .and(ArrayUtil.isNotEmpty(data.getBusinessTypes()),()->tbSysOperLog.businessType.in(Arrays.asList(data.getBusinessTypes())))
                .and(data.getStatus() != null && data.getStatus() > 0,()->tbSysOperLog.status.eq(data.getStatus()))
                .and(StringUtils.isNotBlank(data.getOperName()),()->tbSysOperLog.operName.like(data.getOperName()))
                .build())
                .orderBy(tbSysOperLog.id.desc()).fetch();
        return MapstructUtils.convert(ret, SysOperLog.class);
    }
}
