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

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.api.Request;
import cc.iotkit.data.manager.IAlertConfigData;
import cc.iotkit.data.manager.IAlertRecordData;
import cc.iotkit.model.alert.AlertConfig;
import cc.iotkit.model.alert.AlertRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlertService {

    @Autowired
    private IAlertConfigData alertConfigData;
    @Autowired
    private IAlertRecordData alertRecordData;

    public AlertConfig createAlertConfig(AlertConfig alertConfig) {
        return alertConfigData.save(alertConfig);
    }

    public AlertConfig updateAlertConfig(AlertConfig alertConfig) {
        return alertConfigData.save(alertConfig);
    }

    public void deleteAlertConfigById(Long id) {
        alertConfigData.deleteById(id);
    }

    public Paging<AlertConfig> selectAlertConfigPage(PageRequest<AlertConfig> request) {
        return alertConfigData.selectAlertConfigPage(request);
    }

    public Paging<AlertRecord> selectAlertRecordPage(PageRequest<AlertRecord> request) {
        return alertRecordData.selectAlertConfigPage(request);
    }

    public void addAlert(AlertConfig config, String content) {
        alertRecordData.save(AlertRecord.builder()
                .level(config.getLevel())
                .name(config.getName())
                .readFlg(false)
                .alertTime(System.currentTimeMillis())
                .details(content)
                .build());
    }
}
