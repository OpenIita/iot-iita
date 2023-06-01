package cc.iotkit.data.service;

import cc.iotkit.common.api.Paging;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.SysDeptRepository;
import cc.iotkit.data.model.TbSysNotice;
import cc.iotkit.data.system.ISysNoticeData;
import cc.iotkit.model.system.SysNotice;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author：tfd
 * @Date：2023/5/30 13:43
 */
@Primary
@Service
@RequiredArgsConstructor
public class SysNoticeDataImpl implements ISysNoticeData, IJPACommData<SysNotice, Long> {

    @Autowired
    private SysDeptRepository baseRepository;

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public JpaRepository getBaseRepository() {
        return baseRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbSysNotice.class;
    }


    @Override
    public Paging<SysNotice> findByConditions(String noticeTitle, String noticeType, String status, int page, int size) {
        return null;
    }
}
