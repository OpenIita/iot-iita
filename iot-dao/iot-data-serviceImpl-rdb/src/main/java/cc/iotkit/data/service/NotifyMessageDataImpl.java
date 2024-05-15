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

import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.NotifyMessageRepository;
import cc.iotkit.data.manager.INotifyMessageData;
import cc.iotkit.data.model.TbNotifyMessage;
import cc.iotkit.model.notify.NotifyMessage;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: 石恒
 * @Date: 2023/5/13 18:35
 * @Description:
 */
@Primary
@Service
public class NotifyMessageDataImpl implements INotifyMessageData, IJPACommData<NotifyMessage, Long> {

    @Resource
    private NotifyMessageRepository notifyMessageRepository;


    @Override
    public JpaRepository getBaseRepository() {
        return notifyMessageRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbNotifyMessage.class;
    }

    @Override
    public Class getTClass() {
        return NotifyMessage.class;
    }

}
