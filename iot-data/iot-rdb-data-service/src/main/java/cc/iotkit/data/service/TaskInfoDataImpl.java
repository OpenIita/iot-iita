package cc.iotkit.data.service;

import cc.iotkit.data.ITaskInfoData;
import cc.iotkit.model.Paging;
import cc.iotkit.model.rule.TaskInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskInfoDataImpl implements ITaskInfoData {
    @Override
    public List<TaskInfo> findByUid(String uid) {
        return null;
    }

    @Override
    public Paging<TaskInfo> findByUid(String uid, int page, int size) {
        return null;
    }

    @Override
    public long countByUid(String uid) {
        return 0;
    }

    @Override
    public TaskInfo findById(String s) {
        return null;
    }

    @Override
    public TaskInfo save(TaskInfo data) {
        return null;
    }

    @Override
    public TaskInfo add(TaskInfo data) {
        return null;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<TaskInfo> findAll() {
        return null;
    }

    @Override
    public Paging<TaskInfo> findAll(int page, int size) {
        return null;
    }
}
