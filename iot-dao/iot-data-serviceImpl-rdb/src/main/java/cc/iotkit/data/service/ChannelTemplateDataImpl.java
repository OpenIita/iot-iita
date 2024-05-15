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
import cc.iotkit.data.dao.ChannelTemplateRepository;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.IChannelTemplateData;
import cc.iotkit.data.model.TbChannelTemplate;
import cc.iotkit.model.notify.ChannelTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * author: 石恒
 * date: 2023-05-11 17:45
 * description:
 **/
@Primary
@Service
public class ChannelTemplateDataImpl implements IChannelTemplateData, IJPACommData<ChannelTemplate, Long> {

    @Resource
    private ChannelTemplateRepository channelTemplateRepository;

    @Override
    public JpaRepository getBaseRepository() {
        return channelTemplateRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbChannelTemplate.class;
    }

    @Override
    public Class getTClass() {
        return ChannelTemplate.class;
    }


    @Override
    public void batchSave(List<ChannelTemplate> data) {

    }

    @Override
    public long count() {
        return channelTemplateRepository.count();
    }

    @Override
    public List<ChannelTemplate> findAll() {
        return channelTemplateRepository.findAll().stream()
                .map(c -> MapstructUtils.convert(c, ChannelTemplate.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ChannelTemplate> findAllByCondition(ChannelTemplate data) {
        return Collections.emptyList();
    }

    @Override
    public ChannelTemplate findOneByCondition(ChannelTemplate data) {
        return null;
    }


}
