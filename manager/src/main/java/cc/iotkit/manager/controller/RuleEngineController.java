package cc.iotkit.manager.controller;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.dao.*;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.manager.utils.AuthUtil;
import cc.iotkit.model.Paging;
import cc.iotkit.model.rule.SceneInfo;
import cc.iotkit.model.rule.SceneLog;
import cc.iotkit.model.rule.TaskInfo;
import cc.iotkit.model.rule.TaskLog;
import cc.iotkit.ruleengine.scene.SceneManager;
import cc.iotkit.ruleengine.task.TaskManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private SceneInfoRepository sceneInfoRepository;

    @Autowired
    private SceneLogRepository sceneLogRepository;

    @Autowired
    private SceneLogDao sceneLogDao;

    @Autowired
    private DataOwnerService dataOwnerService;

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private SceneManager sceneManager;

    @Autowired
    private TaskLogDao taskLogDao;

    @Autowired
    private TaskLogRepository taskLogRepository;

    @PostMapping("/scenes")
    public List<SceneInfo> scenes() {
        return sceneInfoRepository.findAll(Example.of(dataOwnerService
                .wrapExample(new SceneInfo()))
        );
    }

    @PostMapping("/saveScene")
    public void saveScene(@RequestBody SceneInfo scene) {
        if (StringUtils.isBlank(scene.getId())) {
            scene.setId(UUID.randomUUID().toString());
            scene.setState(SceneInfo.STATE_STOPPED);
            scene.setCreateAt(System.currentTimeMillis());
            scene.setUid(AuthUtil.getUserId());
            sceneInfoRepository.save(scene);
            sceneManager.add(scene);
        } else {
            Optional<SceneInfo> oldScene = sceneInfoRepository.findById(scene.getId());
            if (!oldScene.isPresent()) {
                throw new BizException("Scene does not exist");
            }
            SceneInfo sceneInfo = oldScene.get();
            if (SceneInfo.STATE_RUNNING.equals(sceneInfo.getState())) {
                throw new BizException("Scene is running");
            }

            dataOwnerService.checkOwner(sceneInfo);

            sceneInfo.setListeners(scene.getListeners());
            sceneInfo.setFilters(scene.getFilters());
            sceneInfo.setActions(scene.getActions());
            sceneInfo.setName(scene.getName());
            sceneInfo.setDesc(scene.getDesc());

            sceneInfoRepository.save(sceneInfo);
        }
    }

    @PostMapping("/scene/{sceneId}/pause")
    public void pauseScene(@PathVariable("sceneId") String sceneId) {
        Optional<SceneInfo> sceneOpt = sceneInfoRepository.findById(sceneId);
        if (!sceneOpt.isPresent()) {
            throw new BizException("Scene does not exist");
        }
        SceneInfo sceneInfo = sceneOpt.get();
        dataOwnerService.checkOwner(sceneInfo);
        sceneInfo.setState(SceneInfo.STATE_STOPPED);
        sceneInfoRepository.save(sceneInfo);
        sceneManager.pause(sceneInfo.getId());
    }

    @PostMapping("/scene/{sceneId}/resume")
    public void resumeScene(@PathVariable("sceneId") String sceneId) {
        Optional<SceneInfo> sceneOpt = sceneInfoRepository.findById(sceneId);
        if (!sceneOpt.isPresent()) {
            throw new BizException("Scene does not exist");
        }
        SceneInfo sceneInfo = sceneOpt.get();
        dataOwnerService.checkOwner(sceneInfo);
        sceneInfo.setState(SceneInfo.STATE_RUNNING);
        sceneInfoRepository.save(sceneInfo);
        sceneManager.resume(sceneInfo);
    }

    @DeleteMapping("/scene/{sceneId}/delete")
    public void deleteScene(@PathVariable("sceneId") String sceneId) {
        Optional<SceneInfo> sceneOpt = sceneInfoRepository.findById(sceneId);
        if (!sceneOpt.isPresent()) {
            throw new BizException("Scene does not exist");
        }
        SceneInfo sceneInfo = sceneOpt.get();
        dataOwnerService.checkOwner(sceneInfo);
        sceneInfoRepository.delete(sceneInfo);
        sceneManager.remove(sceneInfo.getId());
        sceneLogDao.deleteLogs(sceneId);
    }

    @PostMapping("/scene/{sceneId}/logs/{size}/{page}")
    public Paging<SceneLog> getSceneLogs(
            @PathVariable("sceneId") String sceneId,
            @PathVariable("size") int size,
            @PathVariable("page") int page
    ) {
        SceneLog sceneLog=new SceneLog();
        sceneLog.setSceneId(sceneId);
        Page<SceneLog> sceneLogs = sceneLogRepository.findAll(Example.of(sceneLog),
                PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("logAt"))));
        return new Paging<>(sceneLogs.getTotalElements(), sceneLogs.getContent());
    }

    @DeleteMapping("/scene/{sceneId}/logs/clear")
    public void clearSceneLogs(@PathVariable("sceneId") String sceneId) {
        sceneLogDao.deleteLogs(sceneId);
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
            if (!taskOpt.isPresent()) {
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
        if (!optionalTaskInfo.isPresent()) {
            throw new BizException("Task does not exist");
        }
        dataOwnerService.checkOwner(optionalTaskInfo.get());
        taskManager.pauseTask(taskId, "stop by " + AuthUtil.getUserId());
    }

    @PostMapping("/task/{taskId}/resume")
    public void resumeTask(@PathVariable("taskId") String taskId) {
        Optional<TaskInfo> optionalTaskInfo = taskInfoRepository.findById(taskId);
        if (!optionalTaskInfo.isPresent()) {
            throw new BizException("Task does not exist");
        }
        dataOwnerService.checkOwner(optionalTaskInfo.get());
        taskManager.resumeTask(taskId, "resume by " + AuthUtil.getUserId());
    }

    @PostMapping("/task/{taskId}/renew")
    public void renewTask(@PathVariable("taskId") String taskId) {
        Optional<TaskInfo> optionalTaskInfo = taskInfoRepository.findById(taskId);
        if (!optionalTaskInfo.isPresent()) {
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
        if (!optionalTaskInfo.isPresent()) {
            throw new BizException("Task does not exist");
        }
        TaskInfo taskInfo = optionalTaskInfo.get();

        dataOwnerService.checkOwner(taskInfo);
        taskManager.deleteTask(taskId, "delete by " + AuthUtil.getUserId());
        taskInfoRepository.deleteById(taskId);
        taskLogDao.deleteLogs(taskId);
    }

    @PostMapping("/task/{taskId}/logs/{size}/{page}")
    public Paging<TaskLog> getTaskLogs(
            @PathVariable("taskId") String taskId,
            @PathVariable("size") int size,
            @PathVariable("page") int page
    ) {
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(taskId);
        Page<TaskLog> taskLogs = taskLogRepository.findAll(Example.of(taskLog),
                PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("logAt"))));
        return new Paging<>(taskLogs.getTotalElements(), taskLogs.getContent());
    }

    @DeleteMapping("/task/{taskId}/logs/clear")
    public void clearTaskLogs(@PathVariable("taskId") String taskId) {
        taskLogDao.deleteLogs(taskId);
    }

}
