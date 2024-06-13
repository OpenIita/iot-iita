/*
 *
 *  * | Licensed 未经许可不能去掉「OPENIITA」相关版权
 *  * +----------------------------------------------------------------------
 *  * | Author: xw2sy@163.com
 *  * +----------------------------------------------------------------------
 *
 *  Copyright [2024] [OPENIITA]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */

package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.constant.UserConstants;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.SysDictDataRepository;
import cc.iotkit.data.model.TbSysDictData;
import cc.iotkit.data.system.ISysDictData;
import cc.iotkit.data.util.PageBuilder;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.system.SysDictData;
import cn.hutool.core.util.ObjectUtil;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static cc.iotkit.data.model.QTbSysDictData.tbSysDictData;

/**
 * @Author：tfd
 * @Date：2023/5/30 13:43
 */
@Primary
@Service
@RequiredArgsConstructor
public class SysDictDataImpl implements ISysDictData, IJPACommData<SysDictData, Long> {

    @Autowired
    private SysDictDataRepository baseRepository;

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
        List<TbSysDictData> rets=jpaQueryFactory.select(tbSysDictData).from(tbSysDictData)
                .where(buildQueryCondition(query)).orderBy(tbSysDictData.dictSort.asc())
                .fetch();
        return MapstructUtils.convert(rets,SysDictData.class);
    }

    @Override
    public Paging<SysDictData> findAll(PageRequest<SysDictData> pageRequest) {
        return PageBuilder.toPaging(baseRepository.findAll(buildQueryCondition(pageRequest.getData()), PageBuilder.toPageable(pageRequest))).to(SysDictData.class);
    }

    @Override
    public SysDictData findByDictTypeAndDictValue(String dictType, String dictValue) {
        return null;
    }

    @Override
    public List<SysDictData> findByDicType(String dictType) {
        List<TbSysDictData> rets=jpaQueryFactory.select(tbSysDictData).from(tbSysDictData)
                .where(PredicateBuilder.instance()
                        .and(tbSysDictData.status.eq(UserConstants.DICT_NORMAL))
                        .and(tbSysDictData.dictType.eq(dictType))
                        .build()).orderBy(tbSysDictData.dictSort.asc())
                        .fetch();
        return MapstructUtils.convert(rets,SysDictData.class);
    }

    @Override
    public long countByDicType(String dictType) {
        return 0;
    }

    private Predicate buildQueryCondition(SysDictData dictData) {
        return PredicateBuilder.instance()
                .and(dictData.getDictSort() != null, () -> tbSysDictData.dictSort.eq(dictData.getDictSort()))
                .and(StringUtils.isNotEmpty(dictData.getDictLabel()), () -> tbSysDictData.dictLabel.like(dictData.getDictLabel()))
                .and(StringUtils.isNotEmpty(dictData.getDictType()), () -> tbSysDictData.dictType.eq(dictData.getDictType()))
                .and(StringUtils.isNotEmpty(dictData.getStatus()), () -> tbSysDictData.status.eq(dictData.getStatus()))
                .and(ObjectUtil.isNotNull(dictData.getTenantId()), () -> tbSysDictData.tenantId.eq(dictData.getTenantId())).build();
    }
}
