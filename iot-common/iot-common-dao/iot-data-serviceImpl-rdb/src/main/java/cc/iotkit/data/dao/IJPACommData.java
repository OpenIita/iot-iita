package cc.iotkit.data.dao;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.ICommonData;
import cc.iotkit.data.util.PageBuilder;
import cc.iotkit.model.Id;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @Author: jay
 * @Date: 2023/6/1 9:27
 * @Version: V1.0
 * @Description: 基础数据操作接口
 */
public  interface IJPACommData< T extends Id<ID>, ID> extends ICommonData<T , ID> {


    JpaRepository getBaseRepository();

    Class getJpaRepositoryClass();

    Class getTClass();





    default T findById(ID id) {
        return (T) MapstructUtils.convert(getBaseRepository().findById(id).orElse(null), getTClass());
    }



    @Override
    default List<T> findByIds(Collection<ID> id) {
        List allById = getBaseRepository().findAllById(id);
        return MapstructUtils.convert(allById, getTClass());
    }

    @Override
    default T save(T data) {
        Object o = getBaseRepository().save(MapstructUtils.convert(data, getJpaRepositoryClass()));
        return (T) MapstructUtils.convert(o, getTClass());
    }

    @Override
    default void batchSave(List<T> data) {
        getBaseRepository().saveAll(MapstructUtils.convert(data, getJpaRepositoryClass()));
    }

    @Override
    default void deleteById(ID id) {
        getBaseRepository().deleteById(id);
    }

    @Override
    default void deleteByIds(Collection<ID> ids) {
        getBaseRepository().deleteAllById(ids);
    }

    @Override
    default long count() {
        return getBaseRepository().count();
    }

    @Override
    default List<T> findAll() {
        return MapstructUtils.convert(getBaseRepository().findAll(), getJpaRepositoryClass());
    }

    @Override
    default Paging<T>  findAll(PageRequest<T> pageRequest) {
        Example example = genExample(pageRequest.getData());

        Page<T> all = getBaseRepository().findAll(example, PageBuilder.toPageable(pageRequest));
        return  PageBuilder.toPaging(all, getTClass());
    }

    /**
     * 按条件查询多个结果
     */
    @Override
    default List<T> findAllByCondition(T data) {
        Example example = genExample(data);

        List all = getBaseRepository().findAll(example);
        return MapstructUtils.convert(all, getTClass());
    }

    /**
     * 按条件查询单个结果
     */
    @Override
    default T findOneByCondition(T data) {
        Example example = genExample(data);

        Optional one = getBaseRepository().findOne(example);
        if(one.isPresent()){
            MapstructUtils.convert(one.get(), getTClass());
        }
        return null;
    }


    default Example genExample(T data) {
        return Example.of(MapstructUtils.convert(data, getJpaRepositoryClass()));
    }


}
