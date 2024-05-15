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
import cc.iotkit.data.dao.PluginInstanceRepository;
import cc.iotkit.data.manager.IPluginInstanceData;
import cc.iotkit.data.model.TbPluginInstance;
import cc.iotkit.model.plugin.PluginInstance;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @author sjg
 */
@Primary
@Service
public class PluginInstanceDataImpl implements IPluginInstanceData, IJPACommData<PluginInstance, Long> {

    @Autowired
    private PluginInstanceRepository pluginInstanceRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Override
    public JpaRepository getBaseRepository() {
        return pluginInstanceRepository;
    }

    @Override
    public Class<TbPluginInstance> getJpaRepositoryClass() {
        return TbPluginInstance.class;
    }

    @Override
    public Class<PluginInstance> getTClass() {
        return PluginInstance.class;
    }

    @Override
    public PluginInstance findInstance(String mainId, String pluginId) {
        return MapstructUtils.convert(pluginInstanceRepository.findByMainIdAndPluginId(mainId, pluginId), PluginInstance.class);
    }
}
