package cc.iotkit.comps.service;

import cc.iotkit.common.Constants;
import cc.iotkit.dao.DeviceReportRepository;
import cc.iotkit.model.device.message.DeviceReport;
import cc.iotkit.mq.ConsumerHandler;
import cc.iotkit.mq.MqConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 设备上报记录消费入库
 */
@Slf4j
@Service
public class ReportRecordPersistService implements ConsumerHandler<DeviceReport> {

    @Autowired
    private MqConsumer<DeviceReport> deviceReportMqConsumer;
    @Autowired
    private DeviceReportRepository deviceReportRepository;

    @PostConstruct
    public void init() {
        deviceReportMqConsumer.consume(Constants.DEVICE_REPORT_RECORD_TOPIC, this);
    }

    @Override
    public void handler(DeviceReport msg) {
        try {
            deviceReportRepository.save(msg);
        } catch (Throwable e) {
            log.warn("save report record error", e);
        }
    }

}
