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
import cc.iotkit.data.dao.OauthClientRepository;
import cc.iotkit.data.manager.IOauthClientData;
import cc.iotkit.data.model.TbOauthClient;
import cc.iotkit.model.OauthClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Primary
@Service
public class OauthClientDataImpl implements IOauthClientData, IJPACommData<OauthClient, String> {

    @Autowired
    private OauthClientRepository oauthClientRepository;

    @Override
    public OauthClient findByClientId(String clientId) {
        return MapstructUtils.convert(oauthClientRepository.findByClientId(clientId), OauthClient.class);
    }

    @Override
    public JpaRepository getBaseRepository() {
        return oauthClientRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbOauthClient.class;
    }

    @Override
    public Class getTClass() {
        return OauthClient.class;
    }

    @Override
    public OauthClient findById(String s) {
        return MapstructUtils.convert(oauthClientRepository.findById(s).orElse(null), OauthClient.class);
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





}
