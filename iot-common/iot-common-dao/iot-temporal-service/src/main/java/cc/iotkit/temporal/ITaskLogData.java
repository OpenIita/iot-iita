package cc.iotkit.temporal;

import cc.iotkit.model.Paging;
import cc.iotkit.model.rule.TaskLog;

public interface ITaskLogData {
    void deleteByTaskId(String taskId);

    Paging<TaskLog> findByTaskId(String taskId, int page, int size);

    void add(TaskLog log);
}
