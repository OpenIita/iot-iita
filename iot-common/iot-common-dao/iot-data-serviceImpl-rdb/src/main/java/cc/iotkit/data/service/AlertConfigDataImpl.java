package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.utils.MapstructUtils;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Primary
@Service
public class AlertConfigDataImpl implements IAlertConfigData {

    @Autowired
    private AlertConfigRepository alertConfigRepository;


    @Override
    public AlertConfig findById(String s) {
        return null;
    }

    @Override
    public AlertConfig save(AlertConfig data) {
        alertConfigRepository.save(MapstructUtils.convert(data, TbAlertConfig.class));
        return data;
    }

    @Override
    public AlertConfig add(AlertConfig data) {
        return null;
    }

    @Override
    public void deleteById(String s) {
        alertConfigRepository.deleteById(s);
    }

    @Override
    public void deleteByIds(String[] strings) {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<AlertConfig> findAll() {
        return null;
    }

    @Override
    public Paging<AlertConfig> findAll(int page, int size) {
        return null;
    }

    @Override
    public Paging<AlertConfig> selectAlertConfigPage(PageRequest<AlertConfig> request) {
        Page<TbAlertConfig> alertConfigPage = alertConfigRepository.findAll(Pageable.ofSize(request.getPageSize()).withPage(request.getPageNum() - 1));
        return new Paging<>(alertConfigPage.getTotalElements(), MapstructUtils.convert(alertConfigPage.getContent(), AlertConfig.class));
    }
}
