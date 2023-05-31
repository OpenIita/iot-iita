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

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.manager.IOauthClientData;
import cc.iotkit.data.dao.OauthClientRepository;
import cc.iotkit.data.model.TbOauthClient;
import cc.iotkit.model.OauthClient;
import cc.iotkit.common.api.Paging;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Primary
@Service
public class OauthClientDataImpl implements IOauthClientData {

    @Autowired
    private OauthClientRepository oauthClientRepository;

    @Override
    public OauthClient findByClientId(String clientId) {
        return MapstructUtils.convert(oauthClientRepository.findByClientId(clientId), OauthClient.class);
    }

    @Override
    public OauthClient findById(String s) {
        return MapstructUtils.convert(oauthClientRepository.findById(s).orElse(null), OauthClient.class);
    }

    @Override
    public List<OauthClient> findByIds(Collection<String> id) {
        return null;
    }

    @Override
    public OauthClient save(OauthClient data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
            data.setCreateAt(System.currentTimeMillis());
        }
        oauthClientRepository.save(MapstructUtils.convert(data, TbOauthClient.class));
        return data;
    }

    @Override
    public void batchSave(List<OauthClient> data) {

    }

    @Override
    public void deleteById(String s) {
        oauthClientRepository.deleteById(s);
    }

    @Override
    public void deleteByIds(Collection<String> strings) {

    }


    @Override
    public long count() {
        return oauthClientRepository.count();
    }

    @Override
    public List<OauthClient> findAll() {
        return new ArrayList<>();
    }

    @Override
    public Paging<OauthClient> findAll(PageRequest<OauthClient> pageRequest) {
        return null;
    }

    @Override
    public List<OauthClient> findAllByCondition(OauthClient data) {
        return null;
    }

    @Override
    public OauthClient findOneByCondition(OauthClient data) {
        return null;
    }


}