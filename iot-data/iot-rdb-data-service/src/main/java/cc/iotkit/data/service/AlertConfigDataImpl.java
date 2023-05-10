package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.data.IAlertConfigData;
import cc.iotkit.data.dao.AlertConfigRepository;
import cc.iotkit.data.model.AlertConfigMapper;
import cc.iotkit.data.model.ProductMapper;
import cc.iotkit.data.model.TbAlertConfig;
import cc.iotkit.data.model.TbProduct;
import cc.iotkit.model.Paging;
import cc.iotkit.model.alert.AlertConfig;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        alertConfigRepository.save(AlertConfigMapper.M.toVo(data));
        return data;
    }

    @Override
    public AlertConfig add(AlertConfig data) {
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
    public List<AlertConfig> findAll() {
        return null;
    }

    @Override
    public Paging<AlertConfig> findAll(int page, int size) {
        return null;
    }

    @Override
    public Paging<AlertConfig> selectAlertConfigPage(PageRequest<AlertConfig> request) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(request.getPageNo(), request.getPageSize(), Sort.Direction.DESC, "createAt");
        Page<TbAlertConfig> alertConfigPage =  alertConfigRepository.findAll(Example.of(AlertConfigMapper.M.toVo(request.getData())), pageable);
        return new Paging<>(alertConfigPage.getTotalElements(), AlertConfigMapper.toDto(alertConfigPage.getContent()));
    }
}
