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

import cc.iotkit.data.IRuleInfoData;
import cc.iotkit.data.dao.RuleInfoRepository;
import cc.iotkit.data.model.RuleInfoMapper;
import cc.iotkit.data.model.TbRuleInfo;
import cc.iotkit.model.Paging;
import cc.iotkit.model.rule.RuleInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Primary
@Service
public class RuleInfoDataImpl implements IRuleInfoData {

    @Autowired
    private RuleInfoRepository ruleInfoRepository;

    @Override
    public List<RuleInfo> findByUidAndType(String uid, String type) {
        return RuleInfoMapper.toDto(ruleInfoRepository.findByUidAndType(uid, type));
    }

    @Override
    public Paging<RuleInfo> findByUidAndType(String uid, String type, int page, int size) {
        Page<TbRuleInfo> paged = ruleInfoRepository.findByUidAndType(uid, type,
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                RuleInfoMapper.toDto(paged.getContent()));
    }

    @Override
    public Paging<RuleInfo> findByType(String type, int page, int size) {
        Page<TbRuleInfo> paged = ruleInfoRepository.findByType(type,
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                RuleInfoMapper.toDto(paged.getContent()));
    }

    @Override
    public List<RuleInfo> findByUid(String uid) {
        return RuleInfoMapper.toDto(ruleInfoRepository.findByUid(uid));
    }

    @Override
    public Paging<RuleInfo> findByUid(String uid, int page, int size) {
        Page<TbRuleInfo> paged = ruleInfoRepository.findByUid(uid,
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                RuleInfoMapper.toDto(paged.getContent()));
    }

    @Override
    public long countByUid(String uid) {
        return ruleInfoRepository.countByUid(uid);
    }

    @Override
    public RuleInfo findById(String s) {
        return RuleInfoMapper.toDtoFix(ruleInfoRepository.findById(s).orElse(null));
    }

    @Override
    public RuleInfo save(RuleInfo data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
            data.setCreateAt(System.currentTimeMillis());
        }
        ruleInfoRepository.save(RuleInfoMapper.toVoFix(data));
        return data;
    }

    @Override
    public RuleInfo add(RuleInfo data) {
        return save(data);
    }

    @Override
    public void deleteById(String s) {
        ruleInfoRepository.deleteById(s);
    }

    @Override
    public long count() {
        return ruleInfoRepository.count();
    }

    @Override
    public List<RuleInfo> findAll() {
        return RuleInfoMapper.toDto(ruleInfoRepository.findAll());
    }

    @Override
    public Paging<RuleInfo> findAll(int page, int size) {
        Page<TbRuleInfo> paged = ruleInfoRepository.
                findAll(Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                RuleInfoMapper.toDto(paged.getContent()));
    }
}
