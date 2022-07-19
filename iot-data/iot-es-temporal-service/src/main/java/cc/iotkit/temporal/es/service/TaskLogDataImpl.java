package cc.iotkit.temporal.es.service;

import cc.iotkit.model.Paging;
import cc.iotkit.model.rule.TaskLog;
import cc.iotkit.temporal.ITaskLogData;
import org.springframework.stereotype.Service;

@Service
public class TaskLogDataImpl implements ITaskLogData {
    @Override
    public void deleteByTaskId(String taskId) {

    }

    @Override
    public Paging<TaskLog> findByTaskId(String taskId, int page, int size) {
        return null;
    }

    @Override
    public void add(TaskLog log) {

    }
}
