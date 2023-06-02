package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.SysOssConfigRepository;
import cc.iotkit.data.model.TbSysOssConfig;
import cc.iotkit.data.system.ISysOssConfigData;
import cc.iotkit.data.util.PageBuilder;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.system.SysOssConfig;
import cn.hutool.core.util.ArrayUtil;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static cc.iotkit.data.model.QTbSysOssConfig.tbSysOssConfig;

/**
 * @Author：tfd
 * @Date：2023/5/31 15:24
 */
@Primary
@Service
@RequiredArgsConstructor
public class SysOssConfigDataImpl implements ISysOssConfigData, IJPACommData<SysOssConfig, Long> {

    private final SysOssConfigRepository operLogRepository;


    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public JpaRepository getBaseRepository() {
        return operLogRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbSysOssConfig.class;
    }


}
