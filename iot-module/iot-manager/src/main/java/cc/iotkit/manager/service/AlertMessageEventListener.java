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

package cc.iotkit.manager.service;

import cc.iotkit.data.manager.IAlertConfigData;
import cc.iotkit.message.event.MessageEvent;
import cc.iotkit.message.listener.MessageEventListener;
import cc.iotkit.message.model.Message;
import cc.iotkit.model.alert.AlertConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author sjg
 */
@Slf4j
@Component
public class AlertMessageEventListener implements MessageEventListener {

    @Autowired
    private IAlertConfigData alertConfigData;

    @Autowired
    private AlertService alertService;

    @Override
    @EventListener(classes = MessageEvent.class)
    public void doEvent(MessageEvent event) {
        Message message = event.getMessage();
        AlertConfig alertConfig = alertConfigData.findById(message.getAlertConfigId());
        alertService.addAlert(alertConfig, message.getFormatContent());
    }
}
