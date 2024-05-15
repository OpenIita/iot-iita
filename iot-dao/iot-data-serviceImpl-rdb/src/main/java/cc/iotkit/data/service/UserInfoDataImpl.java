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

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.UserInfoRepository;
import cc.iotkit.data.manager.IUserInfoData;
import cc.iotkit.data.model.TbUserInfo;
import cc.iotkit.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
@RequiredArgsConstructor
public class UserInfoDataImpl implements IUserInfoData, IJPACommData<UserInfo, Long> {

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


}
