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
package cc.iotkit.manager.controller;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.api.Request;
import cc.iotkit.manager.service.AlertService;
import cc.iotkit.model.alert.AlertConfig;
import cc.iotkit.model.alert.AlertRecord;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = {"告警中心"})
@Slf4j
@RestController
@RequestMapping("/alert")
public class AlertController {

    @Autowired
    private AlertService alertService;


    @ApiOperation("新增告警中心配置")
    @SaCheckPermission("iot:alertConfig:add")
    @PostMapping("/createAlertConfig")
    public AlertConfig createAlertConfig(@RequestBody @Validated Request<AlertConfig> request) {
        return alertService.createAlertConfig(request.getData());
    }

    @ApiOperation("编辑告警中心配置")
    @SaCheckPermission("iot:alertConfig:edit")
    @PostMapping("/updateAlertConfig")
    public AlertConfig updateAlertConfig(@RequestBody @Validated Request<AlertConfig> request) {
        return alertService.updateAlertConfig(request.getData());
    }

    @ApiOperation("删除告警中心配置")
    @SaCheckPermission("iot:alertConfig:remove")
    @PostMapping("/deleteAlertConfigById")
    public void deleteAlertConfigById(@RequestBody @Validated Request<Long> request) {
        alertService.deleteAlertConfigById(request.getData());
    }

    @ApiOperation("查询告警中心配置分页")
    @SaCheckPermission("iot:alertConfig:query")
    @PostMapping("/selectAlertConfigPage")
    public Paging<AlertConfig> selectAlertConfigPage(@RequestBody @Validated PageRequest<AlertConfig> request) {
        return alertService.selectAlertConfigPage(request);
    }


    @ApiOperation("查询告警消息分页")
    @SaCheckPermission("iot:alert:query")
    @PostMapping("/selectAlertRecordPage")
    public Paging<AlertRecord> selectAlertRecordPage(@RequestBody @Validated PageRequest<AlertRecord> request) {
        return alertService.selectAlertRecordPage(request);
    }


}
