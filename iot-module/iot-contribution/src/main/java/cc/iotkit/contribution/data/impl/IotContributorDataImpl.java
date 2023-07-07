package cc.iotkit.contribution.data.impl;

import cc.iotkit.contribution.repository.IotContributorRepository;
import cc.iotkit.data.ICommonData;
import cc.iotkit.contribution.data.IIotContributorData;
import cc.iotkit.contribution.data.model.TbIotContributor;
import cc.iotkit.contribution.model.IotContributor;
import java.util.List;

import cc.iotkit.data.util.PageBuilder;
import com.google.common.collect.Lists;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import cc.iotkit.data.util.PredicateBuilder;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.utils.StringUtils;
import java.util.Collection;
import java.util.Optional;

import cc.iotkit.common.utils.MapstructUtils;


import static cc.iotkit.contribution.data.model.QTbIotContributor.tbIotContributor;
/**
 * 数据实现接口
 *
 * @author Lion Li
 * @date 2023-07-04
 */
@Primary
@Service
@RequiredArgsConstructor
public class IotContributorDataImpl implements IIotContributorData {

    private final IotContributorRepository baseRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Paging<IotContributor> findAll(PageRequest<IotContributor> pageRequest) {
        return PageBuilder.toPaging(baseRepository.findAll(buildQueryCondition(pageRequest.getData()), PageBuilder.toPageable(pageRequest))).to(IotContributor.class);
    }

    @Override
    public List<IotContributor> findAllByCondition(IotContributor data) {
        Iterable<TbIotContributor> all = baseRepository.findAll(buildQueryCondition(data));
        return MapstructUtils.convert(Lists.newArrayList(all), IotContributor.class);
    }

    @Override
    public IotContributor findOneByCondition(IotContributor data) {
        Optional<TbIotContributor> one = baseRepository.findOne(buildQueryCondition(data));
        if(one.isPresent()){
            return MapstructUtils.convert(one.get(), IotContributor.class);
        }
        return null;
    }

    private Predicate buildQueryCondition(IotContributor bo) {
        PredicateBuilder builder = PredicateBuilder.instance();

                    builder.and(StringUtils.isNotBlank(bo.getContributor()), ()->tbIotContributor.contributor.eq(bo.getContributor()));
                    builder.and(bo.getPost() != null, ()->tbIotContributor.post.eq(bo.getPost()));
                    builder.and(StringUtils.isNotBlank(bo.getStatus()), ()->tbIotContributor.status.eq(bo.getStatus()));
        return builder.build();
    }

    @Override
    public List<IotContributor> findByIds(Collection<Long> id) {
        List allById = baseRepository.findAllById(id);
        return MapstructUtils.convert(allById, IotContributor.class);
    }

    @Override
    public IotContributor save(IotContributor data) {
        Object o = baseRepository.save(MapstructUtils.convert(data, TbIotContributor.class));
        return MapstructUtils.convert(o, IotContributor.class);
    }

    @Override
    public void batchSave(List<IotContributor> data) {
        baseRepository.saveAll(MapstructUtils.convert(data, TbIotContributor.class));
    }

    @Override
    public void deleteById(Long id) {
        baseRepository.deleteById(id);
    }

    @Override
    public void deleteByIds(Collection<Long> ids) {
        baseRepository.deleteAllById(ids);
    }

    @Override
    public long count() {
        return baseRepository.count();
    }

    @Override
    public List<IotContributor> findAll() {
        return MapstructUtils.convert(baseRepository.findAll(), IotContributor.class);
    }

    @Override
    public IotContributor findById(Long id) {
        TbIotContributor ret = jpaQueryFactory.select(tbIotContributor).from(tbIotContributor).where(tbIotContributor.id.eq(id)).fetchOne();
        IotContributor convert = MapstructUtils.convert(ret, IotContributor.class);
        return convert;
    }
}