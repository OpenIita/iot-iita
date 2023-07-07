package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.AlertRecordRepository;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.IAlertRecordData;
import cc.iotkit.data.model.TbAlertRecord;
import cc.iotkit.model.alert.AlertRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Primary
@Service
public class AlertRecordDataImpl implements IAlertRecordData, IJPACommData<AlertRecord, Long> {

    @Autowired
    private AlertRecordRepository alertRecordRepository;


    @Override
    public JpaRepository getBaseRepository() {
        return alertRecordRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbAlertRecord.class;
    }

    @Override
    public Class getTClass() {
        return AlertRecord.class;
    }


    @Override
    public Paging<AlertRecord> selectAlertConfigPage(PageRequest<AlertRecord> request) {
        Page<TbAlertRecord> alertRecordPage = alertRecordRepository.findAll(Pageable.ofSize(request.getPageSize())
                .withPage(request.getPageNum() - 1));
        return new Paging<>(alertRecordPage.getTotalElements(),
                MapstructUtils.convert(alertRecordPage.getContent(), AlertRecord.class));
    }
}
