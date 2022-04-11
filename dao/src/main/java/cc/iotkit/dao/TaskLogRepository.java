package cc.iotkit.dao;

import cc.iotkit.model.rule.TaskLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskLogRepository extends ElasticsearchRepository<TaskLog, String> {

    void deleteByTaskId(String taskId);

    Page<TaskLog> findByTaskId(String taskId, Pageable pageable);

}
