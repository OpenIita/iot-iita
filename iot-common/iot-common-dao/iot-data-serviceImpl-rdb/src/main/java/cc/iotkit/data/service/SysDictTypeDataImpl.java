package cc.iotkit.data.service;

import cc.iotkit.common.api.Paging;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.SysDeptRepository;
import cc.iotkit.data.model.TbSysDictType;
import cc.iotkit.data.system.ISysDictTypeData;
import cc.iotkit.model.system.SysDictType;
import cc.iotkit.model.system.SysDictType;
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
public class SysDictTypeDataImpl implements ISysDictTypeData, IJPACommData<SysDictType, Long> {

    @Autowired
    private SysDeptRepository baseRepository;

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public JpaRepository getBaseRepository() {
        return baseRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbSysDictType.class;
    }


    @Override
    public List<SysDictType> findByConditions(SysDictType query) {
        return null;
    }

    @Override
    public Paging<SysDictType> findByConditions(SysDictType query, int page, int size) {
        return null;
    }

    @Override
    public SysDictType findByDicType(String dictType) {
        return null;
    }

    @Override
    public void updateDicType(String dictType, String newType) {

    }


}
