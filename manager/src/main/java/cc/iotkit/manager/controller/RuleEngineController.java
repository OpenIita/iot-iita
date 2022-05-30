package cc.iotkit.manager.controller;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.dao.*;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.utils.AuthUtil;
import cc.iotkit.model.Paging;
import cc.iotkit.model.rule.RuleInfo;
import cc.iotkit.model.rule.RuleLog;
import cc.iotkit.model.rule.TaskInfo;
import cc.iotkit.model.rule.TaskLog;
import cc.iotkit.ruleengine.rule.RuleManager;
import cc.iotkit.ruleengine.task.TaskManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/rule_engine")
public class RuleEngineController {

    @Autowired
    private TaskInfoRepository taskInfoRepository;

    @Autowired
    private RuleInfoRepository ruleInfoRepository;

    @Lazy
    @Autowired
    private RuleLogRepository ruleLogRepository;

    @Autowired
    private DataOwnerService dataOwnerService;

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private RuleManager ruleManager;

    @Lazy
    @Autowired
    private TaskLogRepository taskLogRepository;

    @PostMapping("/rules/{type}/{size}/{page}")
    public Paging<RuleInfo> rules(
            @PathVariable("type") String type,
            @PathVariable("size") int size,
            @PathVariable("page") int page
    ) {
        RuleInfo ruleInfo = new RuleInfo();
        ruleInfo.setType(type);
        Page<RuleInfo> rules = ruleInfoRepository.findAll(Example.of(dataOwnerService
                .wrapExample(ruleInfo)), Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(rules.getTotalElements(), rules.getContent());
    }

    @PostMapping("/rule/save")
    public void saveRule(@RequestBody RuleInfo rule) {
        if (StringUtils.isBlank(rule.getId())) {
            rule.setId(UUID.randomUUID().toString());
            rule.setState(RuleInfo.STATE_STOPPED);
            rule.setCreateAt(System.currentTimeMillis());
            rule.setUid(AuthUtil.getUserId());
            ruleInfoRepository.save(rule);
            ruleManager.add(rule);
        } else {
            Optional<RuleInfo> oldRule = ruleInfoRepository.findById(rule.getId());
            if (!oldRule.isPresent()) {
                throw new BizException("Rule does not exist");
            }
            RuleInfo ruleInfo = oldRule.get();
            if (RuleInfo.STATE_RUNNING.equals(ruleInfo.getState())) {
                throw new BizException("Rule is running");
            }

            dataOwnerService.checkOwner(ruleInfo);

            ruleInfo.setListeners(rule.getListeners());
            ruleInfo.setFilters(rule.getFilters());
            ruleInfo.setActions(rule.getActions());
            ruleInfo.setName(rule.getName());
            ruleInfo.setDesc(rule.getDesc());

            ruleInfoRepository.save(ruleInfo);
        }
    }

    @PostMapping("/rule/{ruleId}/pause")
    public void pauseRule(@PathVariable("ruleId") String ruleId) {
        Optional<RuleInfo> ruleOpt = ruleInfoRepository.findById(ruleId);
        if (ruleOpt.isEmpty()) {
            throw new BizException("Rule does not exist");
        }
        RuleInfo ruleInfo = ruleOpt.get();
        dataOwnerService.checkOwner(ruleInfo);
        ruleInfo.setState(RuleInfo.STATE_STOPPED);
        ruleInfoRepository.save(ruleInfo);
        ruleManager.pause(ruleInfo.getId());
    }

    @PostMapping("/rule/{ruleId}/resume")
    public void resumeRule(@PathVariable("ruleId") String ruleId) {
        Optional<RuleInfo> ruleOpt = ruleInfoRepository.findById(ruleId);
        if (ruleOpt.isEmpty()) {
            throw new BizException("Rule does not exist");
        }
        RuleInfo ruleInfo = ruleOpt.get();
        dataOwnerService.checkOwner(ruleInfo);
        ruleInfo.setState(RuleInfo.STATE_RUNNING);
        ruleInfoRepository.save(ruleInfo);
        ruleManager.resume(ruleInfo);
    }

    @DeleteMapping("/rule/{ruleId}/delete")
    public void deleteRule(@PathVariable("ruleId") String ruleId) {
        Optional<RuleInfo> ruleOpt = ruleInfoRepository.findById(ruleId);
        if (ruleOpt.isEmpty()) {
            throw new BizException("Rule does not exist");
        }
        RuleInfo ruleInfo = ruleOpt.get();
        dataOwnerService.checkOwner(ruleInfo);
        ruleInfoRepository.delete(ruleInfo);
        ruleManager.remove(ruleInfo.getId());
        ruleLogRepository.deleteByRuleId(ruleId);
    }

    @PostMapping("/rule/{ruleId}/logs/{size}/{page}")
    public Paging<RuleLog> getRuleLogs(
            @PathVariable("ruleId") String ruleId,
            @PathVariable("size") int size,
            @PathVariable("page") int page
    ) {
        RuleLog ruleLog = new RuleLog();
        ruleLog.setRuleId(ruleId);
        Page<RuleLog> ruleLogs = ruleLogRepository.findByRuleId(ruleId,
                PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("logAt"))));
        return new Paging<>(ruleLogs.getTotalElements(), ruleLogs.getContent());
    }

    @DeleteMapping("/rule/{ruleId}/logs/clear")
    public void clearRuleLogs(@PathVariable("ruleId") String ruleId) {
        ruleLogRepository.deleteByRuleId(ruleId);
    }

    @PostMapping("/tasks")
    public List<TaskInfo> tasks() {
        return taskInfoRepository.findAll(Example.of(dataOwnerService
                .wrapExample(new TaskInfo()))
        );
    }

    @PostMapping("/saveTask")
    public void saveTask(@RequestBody TaskInfo taskInfo) {
        if (StringUtils.isBlank(taskInfo.getId())) {
            taskInfo.setId(UUID.randomUUID().toString());
            taskInfo.setUid(AuthUtil.getUserId());
            taskInfo.setCreateAt(System.currentTimeMillis());
            taskInfo.setState(TaskInfo.STATE_STOP);
        } else {
            Optional<TaskInfo> taskOpt = taskInfoRepository.findById(taskInfo.getId());
            if (taskOpt.isEmpty()) {
                throw new BizException("Task does not exist");
            }
            TaskInfo oldTask = taskOpt.get();
            taskInfo = ReflectUtil.copyNoNulls(taskInfo, oldTask);
            dataOwnerService.checkOwner(taskInfo);
        }

        taskInfoRepository.save(taskInfo);
    }

    @PostMapping("/task/{taskId}/pause")
    public void pauseTask(@PathVariable("taskId") String taskId) {
        Optional<TaskInfo> optionalTaskInfo = taskInfoRepository.findById(taskId);
        if (optionalTaskInfo.isEmpty()) {
            throw new BizException("Task does not exist");
        }
        dataOwnerService.checkOwner(optionalTaskInfo.get());
        taskManager.pauseTask(taskId, "stop by " + AuthUtil.getUserId());
    }

    @PostMapping("/task/{taskId}/resume")
    public void resumeTask(@PathVariable("taskId") String taskId) {
        Optional<TaskInfo> optionalTaskInfo = taskInfoRepository.findById(taskId);
        if (optionalTaskInfo.isEmpty()) {
            throw new BizException("Task does not exist");
        }
        dataOwnerService.checkOwner(optionalTaskInfo.get());
        taskManager.resumeTask(taskId, "resume by " + AuthUtil.getUserId());
    }

    @PostMapping("/task/{taskId}/renew")
    public void renewTask(@PathVariable("taskId") String taskId) {
        Optional<TaskInfo> optionalTaskInfo = taskInfoRepository.findById(taskId);
        if (optionalTaskInfo.isEmpty()) {
            throw new BizException("Task does not exist");
        }
        TaskInfo taskInfo = optionalTaskInfo.get();

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
        Optional<TaskInfo> optionalTaskInfo = taskInfoRepository.findById(taskId);
        if (optionalTaskInfo.isEmpty()) {
            throw new BizException("Task does not exist");
        }
        TaskInfo taskInfo = optionalTaskInfo.get();

        dataOwnerService.checkOwner(taskInfo);
        taskManager.deleteTask(taskId, "delete by " + AuthUtil.getUserId());
        taskInfoRepository.deleteById(taskId);
        taskLogRepository.deleteByTaskId(taskId);
    }

    @PostMapping("/task/{taskId}/logs/{size}/{page}")
    public Paging<TaskLog> getTaskLogs(
            @PathVariable("taskId") String taskId,
            @PathVariable("size") int size,
            @PathVariable("page") int page
    ) {
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(taskId);
        Page<TaskLog> taskLogs = taskLogRepository.findByTaskId(taskId,
                PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("logAt"))));
        return new Paging<>(taskLogs.getTotalElements(), taskLogs.getContent());
    }

    @DeleteMapping("/task/{taskId}/logs/clear")
    public void clearTaskLogs(@PathVariable("taskId") String taskId) {
        taskLogRepository.deleteByTaskId(taskId);
    }

}
