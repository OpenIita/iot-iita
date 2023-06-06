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
import cc.iotkit.common.api.Request;
import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.satoken.utils.AuthUtil;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.data.manager.IRuleInfoData;
import cc.iotkit.data.manager.ITaskInfoData;
import cc.iotkit.manager.dto.bo.ruleinfo.RuleInfoBo;
import cc.iotkit.manager.dto.bo.ruleinfo.RuleLogBo;
import cc.iotkit.manager.dto.bo.taskinfo.TaskInfoBo;
import cc.iotkit.manager.dto.bo.taskinfo.TaskLogBo;
import cc.iotkit.manager.dto.vo.ruleinfo.RuleInfoVo;
import cc.iotkit.manager.dto.vo.ruleinfo.RuleLogVo;
import cc.iotkit.manager.dto.vo.taskinfo.TaskInfoVo;
import cc.iotkit.manager.dto.vo.taskinfo.TaskLogVo;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.common.api.Paging;
import cc.iotkit.manager.service.IRuleEngineService;
import cc.iotkit.model.rule.RuleInfo;
import cc.iotkit.model.rule.RuleLog;
import cc.iotkit.model.rule.TaskInfo;
import cc.iotkit.model.rule.TaskLog;
import cc.iotkit.ruleengine.rule.RuleManager;
import cc.iotkit.ruleengine.task.TaskManager;
import cc.iotkit.temporal.IRuleLogData;
import cc.iotkit.temporal.ITaskLogData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Api(tags = {"规则引擎"})
@Slf4j
@RestController
@RequestMapping("/rule_engine")
public class RuleEngineController {

    @Autowired
    IRuleEngineService ruleEngineService;

    @ApiOperation("规则列表")
    @PostMapping("/list")
    public Paging<RuleInfoVo> rules(
            @Validated @RequestBody
            PageRequest<RuleInfoBo> request
    ) {
        return ruleEngineService.selectPageList(request);
    }

    @ApiOperation("规则编辑")
    @PostMapping("/edit")
    public boolean saveRule(@RequestBody @Validated  Request<RuleInfoBo> ruleInfoBo) {
        return ruleEngineService.saveRule(ruleInfoBo.getData());

    }

    @ApiOperation("暂停规则")
    @PostMapping("/pause")
    public boolean pauseRule(@Validated @RequestBody Request<String> request) {
        String ruleId = request.getData();
        return ruleEngineService.pauseRule(ruleId);
    }

    @ApiOperation("恢复规则")
    @PostMapping("/resume")
    public boolean resumeRule(@Validated @RequestBody Request<String> request) {
        String ruleId = request.getData();
        return ruleEngineService.resumeRule(ruleId);

    }

    @ApiOperation("删除规则")
    @DeleteMapping("/delete")
    public boolean deleteRule(@Validated @RequestBody Request<String> request) {
        String ruleId = request.getData();
        return ruleEngineService.deleteRule(ruleId);
    }

    @ApiOperation("规则日志")
    @PostMapping("/ruleLog/list")
    public Paging<RuleLogVo> getRuleLogs(
           @Validated @RequestBody PageRequest<RuleLogBo> request
    ) {
        return ruleEngineService.selectRuleLogPageList(request);
    }

    @ApiOperation("清理日志")
    @DeleteMapping("/ruleLog//clear")
    public boolean clearRuleLogs(@Validated @RequestBody Request<String> request) {
        String ruleId = request.getData();
        return ruleEngineService.clearRuleLogs(ruleId);

    }

    @ApiOperation("定时任务列表")
    @PostMapping("/tasks/list")
    public Paging<TaskInfoVo> tasks(@Validated @RequestBody PageRequest<TaskInfoBo> request) {
        return ruleEngineService.selectTaskPageList(request);
    }

    @ApiOperation("定时任务编辑")
    @PostMapping("/task/save")
    public boolean saveTask(@Validated @RequestBody Request<TaskInfoBo> taskInfo) {
       return ruleEngineService.saveTask(taskInfo.getData());
    }

    @ApiOperation("停止定时任务")
    @PostMapping("/task/pause")
    public boolean pauseTask(@Validated @RequestBody Request<String> request) {
        String taskId = request.getData();
        return ruleEngineService.pauseTask(taskId);


    }

    @ApiOperation("恢复定时任务")
    @PostMapping("/task/resume")
    public boolean resumeTask(@Validated @RequestBody Request<String> request) {
        return ruleEngineService.resumeTask(request.getData());
    }

    @PostMapping("/task/renew")
    public boolean renewTask(@Validated @RequestBody Request<String> request) {
        String taskId = request.getData();
       return ruleEngineService.renewTask(taskId);

    }


    @DeleteMapping("/task/delete")
    public boolean deleteTask(@Validated @RequestBody Request<String> request) {
        String taskId = request.getData();
        return ruleEngineService.deleteTask(taskId);

    }

    @PostMapping("/taskLogs/list")
    public Paging<TaskLogVo> getTaskLogs(
            @Validated @RequestBody PageRequest<TaskLogBo> request
    ) {
        return ruleEngineService.selectTaskLogPageList(request);

    }

    @DeleteMapping("/taskLogs/clear")
    public boolean clearTaskLogs( @Validated @RequestBody PageRequest<String> request) {
       return ruleEngineService.clearTaskLogs(request.getData());
    }

}
