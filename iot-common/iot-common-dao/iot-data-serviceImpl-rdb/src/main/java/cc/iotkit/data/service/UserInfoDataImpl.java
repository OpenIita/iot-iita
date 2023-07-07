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
import cc.iotkit.data.dao.UserInfoRepository;
import cc.iotkit.data.manager.IUserInfoData;
import cc.iotkit.data.model.TbUserInfo;
import cc.iotkit.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Primary
@Service
@RequiredArgsConstructor
public class UserInfoDataImpl implements IUserInfoData, IJPACommData<UserInfo, String> {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserInfo findByUid(String uid) {
        return MapstructUtils.convert(userInfoRepository.findByUid(uid), UserInfo.class);
    }

    @Override
    public List<UserInfo> findByType(int type) {
        return MapstructUtils.convert(userInfoRepository.findByType(type), UserInfo.class);
    }

    @Override
    public List<UserInfo> findByTypeAndOwnerId(int type, String ownerId) {
        return MapstructUtils.convert(userInfoRepository.findByTypeAndOwnerId(type, ownerId), UserInfo.class);
    }

    @Override
    public JpaRepository getBaseRepository() {
        return userInfoRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbUserInfo.class;
    }

    @Override
    public Class getTClass() {
        return UserInfo.class;
    }

    @Override
    public UserInfo findById(String s) {
        return MapstructUtils.convert(userInfoRepository.findById(s).orElse(null), UserInfo.class);
    }

    @Override
    public List<UserInfo> findByIds(Collection<String> id) {
        return null;
    }

    @Override
    public UserInfo save(UserInfo data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
            data.setCreateAt(System.currentTimeMillis());
        }
        userInfoRepository.save(MapstructUtils.convert(data, TbUserInfo.class));
        return data;
    }


    @Override
    public void deleteById(String s) {
        userInfoRepository.deleteById(s);
    }



    @Override
    public List<UserInfo> findAll() {
        return MapstructUtils.convert(userInfoRepository.findAll(), UserInfo.class);
    }




}
