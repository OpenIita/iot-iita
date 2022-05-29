package cc.iotkit.dao;

import cc.iotkit.model.device.message.DeviceReport;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceReportRepository extends ElasticsearchRepository<DeviceReport, String> {

    long countByUid(String uid);

}
