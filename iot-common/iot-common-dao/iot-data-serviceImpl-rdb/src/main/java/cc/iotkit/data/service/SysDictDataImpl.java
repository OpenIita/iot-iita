package cc.iotkit.data.service;

import cc.iotkit.common.api.Paging;
import cc.iotkit.common.constant.UserConstants;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.SysDeptRepository;
import cc.iotkit.data.model.TbSysDept;
import cc.iotkit.data.model.TbSysDictData;
import cc.iotkit.data.system.ISysDictData;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.system.SysDept;
import cc.iotkit.model.system.SysDictData;
import cc.iotkit.model.system.SysDictType;
import cn.hutool.core.util.ObjectUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static cc.iotkit.data.model.QTbSysDept.tbSysDept;

/**
 * @Author：tfd
 * @Date：2023/5/30 13:43
 */
@Primary
@Service
@RequiredArgsConstructor
public class SysDictDataImpl implements ISysDictData, IJPACommData<SysDictData, Long> {

    @Autowired
    private SysDeptRepository baseRepository;

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public JpaRepository getBaseRepository() {
        return baseRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbSysDictData.class;
    }

    @Override
    public Class getTClass() {
        return SysDictData.class;
    }


    @Override
    public List<SysDictData> findByConditions(SysDictData query) {
        return findAllByCondition(query);
    }



    @Override
    public SysDictData findByDictTypeAndDictValue(String dictType, String dictValue) {
        return null;
    }

    @Override
    public List<SysDictType> findByDicType(String dictType) {
        return null;
    }

    @Override
    public long countByDicType(String dictType) {
        return 0;
    }
}
