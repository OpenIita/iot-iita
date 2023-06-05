/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.data.service;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.IRuleInfoData;
import cc.iotkit.data.dao.RuleInfoRepository;
import cc.iotkit.data.model.TbRuleInfo;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.rule.RuleInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Primary
@Service
public class RuleInfoDataImpl implements IRuleInfoData, IJPACommData<RuleInfo, String> {

    @Autowired
    private RuleInfoRepository ruleInfoRepository;

    @Override
    public JpaRepository getBaseRepository() {
        return ruleInfoRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbRuleInfo.class;
    }

    @Override
    public Class getTClass() {
        return RuleInfo.class;
    }

    @Override
    public List<RuleInfo> findByUidAndType(String uid, String type) {
        return MapstructUtils.convert(ruleInfoRepository.findByUidAndType(uid, type), RuleInfo.class);
    }

    @Override
    public Paging<RuleInfo> findByUidAndType(String uid, String type, int page, int size) {
        Page<TbRuleInfo> paged = ruleInfoRepository.findByUidAndType(uid, type,
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                MapstructUtils.convert(paged.getContent(), RuleInfo.class));
    }

    @Override
    public Paging<RuleInfo> findByType(String type, int page, int size) {
        Page<TbRuleInfo> paged = ruleInfoRepository.findByType(type,
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                MapstructUtils.convert(paged.getContent(), RuleInfo.class));
    }

    @Override
    public List<RuleInfo> findByUid(String uid) {
        return MapstructUtils.convert(ruleInfoRepository.findByUid(uid), RuleInfo.class);
    }

    @Override
    public Paging<RuleInfo> findByUid(String uid, int page, int size) {
        Page<TbRuleInfo> paged = ruleInfoRepository.findByUid(uid,
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                MapstructUtils.convert(paged.getContent(), RuleInfo.class));
    }

    @Override
    public long countByUid(String uid) {
        return ruleInfoRepository.countByUid(uid);
    }


    @Override
    public RuleInfo findById(String s) {
        return MapstructUtils.convert(ruleInfoRepository.findById(s).orElse(null), RuleInfo.class);
    }

    @Override
    public List<RuleInfo> findByIds(Collection<String> id) {
        return null;
    }

    @Override
    public RuleInfo save(RuleInfo data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
            data.setCreateAt(System.currentTimeMillis());
        }
        ruleInfoRepository.save(MapstructUtils.convert(data, TbRuleInfo.class));
        return data;
    }


}
