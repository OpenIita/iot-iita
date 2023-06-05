package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.IAlertConfigData;
import cc.iotkit.data.dao.AlertConfigRepository;
import cc.iotkit.data.model.TbAlertConfig;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.alert.AlertConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Primary
@Service
public class AlertConfigDataImpl implements IAlertConfigData, IJPACommData<AlertConfig, String> {

    @Autowired
    private AlertConfigRepository alertConfigRepository;


    @Override
    public JpaRepository getBaseRepository() {
        return alertConfigRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbAlertConfig.class;
    }

    @Override
    public Class getTClass() {
        return AlertConfig.class;
    }


    @Override
    public Paging<AlertConfig> selectAlertConfigPage(PageRequest<AlertConfig> request) {
        Page<TbAlertConfig> alertConfigPage = alertConfigRepository.findAll(Pageable.ofSize(request.getPageSize()).withPage(request.getPageNum() - 1));
        return new Paging<>(alertConfigPage.getTotalElements(), MapstructUtils.convert(alertConfigPage.getContent(), AlertConfig.class));
    }
}
