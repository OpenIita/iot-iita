package cc.iotkit.data.service;


import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.SysConfigRepository;
import cc.iotkit.data.model.TbSysConfig;
import cc.iotkit.data.system.ISysConfigData;
import cc.iotkit.data.util.PageBuilder;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.system.SysConfig;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.IteratorUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static cc.iotkit.data.model.QTbSysConfig.tbSysConfig;

@Primary
@Service
@RequiredArgsConstructor
public class SysConfigDataImpl implements ISysConfigData, IJPACommData<SysConfig, Long> {

    private final SysConfigRepository baseRepository;


    @Override
    public JpaRepository getBaseRepository() {
        return baseRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbSysConfig.class;
    }

    @Override
    public SysConfig findById(Long id) {
        TbSysConfig tbSysConfig = baseRepository.findById(id).orElseThrow(() ->
                new BizException(ErrCode.DATA_NOT_EXIST));
        return MapstructUtils.convert(tbSysConfig, SysConfig.class);
    }

    @Override
    public SysConfig save(SysConfig data) {
        baseRepository.save(MapstructUtils.convert(data, TbSysConfig.class));
        return data;
    }

    @Override
    public List<SysConfig> findByIds(Collection<Long> id) {
        Iterable<TbSysConfig> allById = baseRepository.findAllById(id);
        Iterator<TbSysConfig> iterator = allById.iterator();
        return MapstructUtils.convert(IteratorUtils.toList(iterator), SysConfig.class);
    }



    @Override
    public Paging<SysConfig> findAll(PageRequest<SysConfig> pageRequest) {
        SysConfig query = pageRequest.getData();
        Predicate predicate = PredicateBuilder.instance()
                .and(StringUtils.isNotEmpty(query.getConfigName()), () -> tbSysConfig.configName.like(query.getConfigName()))
                .and(StringUtils.isNotEmpty(query.getConfigType()), () -> tbSysConfig.configType.eq(query.getConfigType()))
                .and(StringUtils.isNotEmpty(query.getConfigKey()), () -> tbSysConfig.configKey.like(query.getConfigKey()))
                .build();
        Page<TbSysConfig> all = baseRepository.findAll(predicate, PageBuilder.toPageable(pageRequest));
        return PageBuilder.toPaging(all, SysConfig.class);
    }


    @Override
    public List<SysConfig> findAllByCondition(SysConfig data) {
        Predicate predicate = PredicateBuilder.instance()
                .and(StringUtils.isNotEmpty(data.getConfigKey()), () -> tbSysConfig.configKey.eq(data.getConfigKey()))
                .build();
        Iterator<TbSysConfig> iterator = baseRepository.findAll(predicate).iterator();
        return MapstructUtils.convert(IteratorUtils.toList(iterator), SysConfig.class);
    }

    @Override
    public SysConfig findOneByCondition(SysConfig data) {
        Predicate predicate = PredicateBuilder.instance()
                .and(StringUtils.isNotEmpty(data.getConfigKey()), () -> tbSysConfig.configKey.eq(data.getConfigKey()))
                .build();
        TbSysConfig tbSysConfig = baseRepository.findOne(predicate).orElseThrow(() -> new BizException(ErrCode.DATA_NOT_EXIST));
        return MapstructUtils.convert(tbSysConfig, SysConfig.class);
    }

    @Override
    public SysConfig findByConfigKey(String configKey) {
        TbSysConfig tbSysConfig = baseRepository.findByConfigKey(configKey).orElseThrow(() ->
                new BizException(ErrCode.DATA_NOT_EXIST));
        return MapstructUtils.convert(tbSysConfig, SysConfig.class);
    }
}
