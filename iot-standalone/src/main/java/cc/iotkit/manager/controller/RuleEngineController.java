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

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.data.IRuleInfoData;
import cc.iotkit.data.ITaskInfoData;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.model.Paging;
import cc.iotkit.model.rule.RuleInfo;
import cc.iotkit.model.rule.RuleLog;
import cc.iotkit.model.rule.TaskInfo;
import cc.iotkit.model.rule.TaskLog;
import cc.iotkit.ruleengine.rule.RuleManager;
import cc.iotkit.ruleengine.task.TaskManager;
import cc.iotkit.temporal.IRuleLogData;
import cc.iotkit.temporal.ITaskLogData;
import cc.iotkit.utils.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/rule_engine")
public class RuleEngineController {

    @Autowired
    private ITaskInfoData taskInfoData;

    @Autowired
    private IRuleInfoData ruleInfoData;

    @Autowired
    private IRuleLogData ruleLogData;

    @Autowired
    private DataOwnerService dataOwnerService;

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private RuleManager ruleManager;

    @Autowired
    private ITaskLogData taskLogData;

    @PostMapping("/rules/{type}/{size}/{page}")
    public Paging<RuleInfo> rules(
            @PathVariable("type") String type,
            @PathVariable("size") int size,
            @PathVariable("page") int page
    ) {
        RuleInfo ruleInfo = new RuleInfo();
        ruleInfo.setType(type);
        if (AuthUtil.isAdmin()) {
            return ruleInfoData.findByType(type, page, size);
        } else {
            return ruleInfoData.findByUidAndType(AuthUtil.getUserId(), type, page, size);
        }
    }

    @PostMapping("/rule/save")
    public void saveRule(@RequestBody RuleInfo rule) {
        if (StringUtils.isBlank(rule.getId())) {
            rule.setId(UUID.randomUUID().toString());
            rule.setState(RuleInfo.STATE_STOPPED);
            rule.setCreateAt(System.currentTimeMillis());
            rule.setUid(AuthUtil.getUserId());
            ruleInfoData.save(rule);
            ruleManager.add(rule);
        } else {
            RuleInfo ruleInfo = ruleInfoData.findById(rule.getId());
            if (ruleInfo == null) {
                throw new BizException("Rule does not exist");
            }
            if (RuleInfo.STATE_RUNNING.equals(ruleInfo.getState())) {
                throw new BizException("Rule is running");
            }

            dataOwnerService.checkOwner(ruleInfo);

            ruleInfo.setListeners(rule.getListeners());
            ruleInfo.setFilters(rule.getFilters());
            ruleInfo.setActions(rule.getActions());
            ruleInfo.setName(rule.getName());
            ruleInfo.setDesc(rule.getDesc());

            ruleInfoData.save(ruleInfo);
        }
    }

    @PostMapping("/rule/{ruleId}/pause")
    public void pauseRule(@PathVariable("ruleId") String ruleId) {
        RuleInfo ruleInfo = ruleInfoData.findById(ruleId);
        if (ruleInfo == null) {
            throw new BizException("Rule does not exist");
        }
        dataOwnerService.checkOwner(ruleInfo);
        ruleInfo.setState(RuleInfo.STATE_STOPPED);
        ruleInfoData.save(ruleInfo);
        ruleManager.pause(ruleInfo.getId());
    }

    @PostMapping("/rule/{ruleId}/resume")
    public void resumeRule(@PathVariable("ruleId") String ruleId) {
        RuleInfo ruleInfo = ruleInfoData.findById(ruleId);
        if (ruleInfo == null) {
            throw new BizException("Rule does not exist");
        }
        dataOwnerService.checkOwner(ruleInfo);
        ruleInfo.setState(RuleInfo.STATE_RUNNING);
        ruleInfoData.save(ruleInfo);
        ruleManager.resume(ruleInfo);
    }

    @DeleteMapping("/rule/{ruleId}/delete")
    public void deleteRule(@PathVariable("ruleId") String ruleId) {
        RuleInfo ruleInfo = ruleInfoData.findById(ruleId);
        if (ruleInfo == null) {
            throw new BizException("Rule does not exist");
        }
        dataOwnerService.checkOwner(ruleInfo);
        ruleInfoData.deleteById(ruleInfo.getId());
        ruleManager.remove(ruleInfo.getId());
        ruleLogData.deleteByRuleId(ruleId);
    }

    @PostMapping("/rule/{ruleId}/logs/{size}/{page}")
    public Paging<RuleLog> getRuleLogs(
            @PathVariable("ruleId") String ruleId,
            @PathVariable("size") int size,
            @PathVariable("page") int page
    ) {
        RuleLog ruleLog = new RuleLog();
        ruleLog.setRuleId(ruleId);
        return ruleLogData.findByRuleId(ruleId, page, size);
    }

    @DeleteMapping("/rule/{ruleId}/logs/clear")
    public void clearRuleLogs(@PathVariable("ruleId") String ruleId) {
        ruleLogData.deleteByRuleId(ruleId);
    }

    @PostMapping("/tasks")
    public List<TaskInfo> tasks() {
        if (AuthUtil.isAdmin()) {
            return taskInfoData.findAll();
        }
        return taskInfoData.findByUid(AuthUtil.getUserId());
    }

    @PostMapping("/saveTask")
    public void saveTask(@RequestBody TaskInfo taskInfo) {
        if (StringUtils.isBlank(taskInfo.getId())) {
            taskInfo.setId(UUID.randomUUID().toString());
            taskInfo.setUid(AuthUtil.getUserId());
            taskInfo.setCreateAt(System.currentTimeMillis());
            taskInfo.setState(TaskInfo.STATE_STOP);
        } else {
            TaskInfo oldTask = taskInfoData.findById(taskInfo.getId());
            if (oldTask == null) {
                throw new BizException("Task does not exist");
            }
            taskInfo = ReflectUtil.copyNoNulls(taskInfo, oldTask);
            dataOwnerService.checkOwner(taskInfo);
        }

        taskInfoData.save(taskInfo);
    }

    @PostMapping("/task/{taskId}/pause")
    public void pauseTask(@PathVariable("taskId") String taskId) {
        TaskInfo taskInfo = taskInfoData.findById(taskId);
        if (taskInfo == null) {
            throw new BizException("Task does not exist");
        }
        dataOwnerService.checkOwner(taskInfo);
        taskManager.pauseTask(taskId, "stop by " + AuthUtil.getUserId());
    }

    @PostMapping("/task/{taskId}/resume")
    public void resumeTask(@PathVariable("taskId") String taskId) {
        TaskInfo taskInfo = taskInfoData.findById(taskId);
        if (taskInfo == null) {
            throw new BizException("Task does not exist");
        }
        dataOwnerService.checkOwner(taskInfo);
        taskManager.resumeTask(taskId, "resume by " + AuthUtil.getUserId());
    }

    @PostMapping("/task/{taskId}/renew")
    public void renewTask(@PathVariable("taskId") String taskId) {
        TaskInfo taskInfo = taskInfoData.findById(taskId);
        if (taskInfo == null) {
            throw new BizException("Task does not exist");
        }
        dataOwnerService.checkOwner(taskInfo);
        try {
            taskManager.renewTask(taskInfo);
            taskManager.updateTaskState(taskId, TaskInfo.STATE_RUNNING, "renew by " + AuthUtil.getUserId());
        } catch (SchedulerException e) {
            log.error("renew task error", e);
            throw new BizException("renew task error");
        }
    }


    @DeleteMapping("/task/{taskId}/delete")
    public void deleteTask(@PathVariable("taskId") String taskId) {
        TaskInfo taskInfo = taskInfoData.findById(taskId);
        if (taskInfo == null) {
            throw new BizException("Task does not exist");
        }

        dataOwnerService.checkOwner(taskInfo);
        taskManager.deleteTask(taskId, "delete by " + AuthUtil.getUserId());
        taskInfoData.deleteById(taskId);
        taskLogData.deleteByTaskId(taskId);
    }

    @PostMapping("/task/{taskId}/logs/{size}/{page}")
    public Paging<TaskLog> getTaskLogs(
            @PathVariable("taskId") String taskId,
            @PathVariable("size") int size,
            @PathVariable("page") int page
    ) {
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(taskId);
        return taskLogData.findByTaskId(taskId, page, size);
    }

    @DeleteMapping("/task/{taskId}/logs/clear")
    public void clearTaskLogs(@PathVariable("taskId") String taskId) {
        taskLogData.deleteByTaskId(taskId);
    }

}
