/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.controller;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.api.Request;
import cc.iotkit.manager.service.AlertService;
import cc.iotkit.model.alert.AlertConfig;
import cc.iotkit.model.alert.AlertRecord;
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
    @PostMapping("/createAlertConfig")
    public AlertConfig createAlertConfig(@RequestBody @Validated Request<AlertConfig> request) {
        return alertService.createAlertConfig(request);
    }

    @ApiOperation("编辑告警中心配置")
    @PostMapping("/updateAlertConfig")
    public AlertConfig updateAlertConfig(@RequestBody @Validated Request<AlertConfig> request) {
        return alertService.updateAlertConfig(request);
    }

    @ApiOperation("删除告警中心配置")
    @PostMapping("/deleteAlertConfigById")
    public Boolean deleteAlertConfigById(@RequestBody @Validated Request<Long> request) {
        return alertService.deleteAlertConfigById(request);
    }

    @ApiOperation("查询告警中心配置分页")
    @PostMapping("/selectAlertConfigPage")
    public Paging<AlertConfig> selectAlertConfigPage(@RequestBody @Validated PageRequest<AlertConfig> request) {
        return alertService.selectAlertConfigPage(request);
    }


    @ApiOperation("查询告警消息分页")
    @PostMapping("/selectAlertRecordPage")
    public Paging<AlertRecord> selectAlertRecordPage(@RequestBody @Validated PageRequest<AlertRecord> request) {
        return alertService.selectAlertRecordPage(request);
    }




}
