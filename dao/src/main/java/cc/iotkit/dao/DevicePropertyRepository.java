package cc.iotkit.dao;

import cc.iotkit.model.device.message.DeviceProperty;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevicePropertyRepository extends ElasticsearchRepository<DeviceProperty, String> {


}
