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
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.IUserInfoData;
import cc.iotkit.data.dao.UserInfoRepository;
import cc.iotkit.data.model.TbUserInfo;
import cc.iotkit.data.service.convert.UserInfoMapper;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.UserInfo;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserInfoDataImpl implements IUserInfoData, IJPACommData<UserInfo, String> {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserInfo findByUid(String uid) {
        return UserInfoMapper.toDtoFix(userInfoRepository.findByUid(uid));
    }

    @Override
    public List<UserInfo> findByType(int type) {
        return UserInfoMapper.toDto(userInfoRepository.findByType(type));
    }

    @Override
    public List<UserInfo> findByTypeAndOwnerId(int type, String ownerId) {
        return UserInfoMapper.toDto(userInfoRepository.findByTypeAndOwnerId(type, ownerId));
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
    public UserInfo findById(String s) {
        return UserInfoMapper.toDtoFix(userInfoRepository.findById(s).orElse(null));
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
        userInfoRepository.save(UserInfoMapper.toVoFix(data));
        return data;
    }


    @Override
    public void deleteById(String s) {
        userInfoRepository.deleteById(s);
    }



    @Override
    public List<UserInfo> findAll() {
        return UserInfoMapper.toDto(userInfoRepository.findAll());
    }




}
