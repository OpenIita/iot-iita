package cc.iotkit.dao;

import cc.iotkit.model.device.VirtualDeviceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualDeviceLogRepository extends ElasticsearchRepository<VirtualDeviceLog, String> {

    Page<VirtualDeviceLog> findByVirtualDeviceId(String virtualDeviceId, Pageable pageable);

}
