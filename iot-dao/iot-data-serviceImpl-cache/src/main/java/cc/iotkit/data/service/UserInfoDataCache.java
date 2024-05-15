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
import cc.iotkit.common.constant.Constants;
import cc.iotkit.data.cache.UserInfoCacheEvict;
import cc.iotkit.data.manager.IUserInfoData;
import cc.iotkit.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Qualifier("userInfoDataCache")
public class UserInfoDataCache implements IUserInfoData {

    @Autowired
    private IUserInfoData userInfoData;
    @Autowired
    private UserInfoCacheEvict userInfoCacheEvict;

    @Override
    @Cacheable(value = Constants.CACHE_USER_INFO, key = "#root.method.name+#uid", unless = "#result == null")
    public UserInfo findByUid(String uid) {
        return userInfoData.findByUid(uid);
    }

    @Override
    public List<UserInfo> findByType(int type) {
        return userInfoData.findByType(type);
    }

    @Override
    public UserInfo findById(Long s) {
        return userInfoData.findById(s);
    }

    @Override
    public List<UserInfo> findByIds(Collection<Long> id) {
        return null;
    }

    @Override
    public UserInfo save(UserInfo data) {
        UserInfo userInfo = userInfoData.save(data);
        //清除缓存
        userInfoCacheEvict.findByUid(data.getUid());
        return userInfo;
    }

    @Override
    public void batchSave(List<UserInfo> data) {

    }

    @Override
    public void deleteById(Long s) {
        userInfoData.deleteById(s);
    }

    @Override
    public void deleteByIds(Collection<Long> longs) {

    }

    @Override
    public long count() {
        return userInfoData.count();
    }

    @Override
    public List<UserInfo> findAll() {
        return userInfoData.findAll();
    }

    @Override
    public Paging<UserInfo> findAll(PageRequest<UserInfo> pageRequest) {
        return userInfoData.findAll(pageRequest);
    }

    @Override
    public List<UserInfo> findAllByCondition(UserInfo data) {
        return null;
    }

    @Override
    public UserInfo findOneByCondition(UserInfo data) {
        return null;
    }

}
