package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.data.IAlertRecordData;
import cc.iotkit.data.IHomeData;
import cc.iotkit.data.dao.AlertRecordRepository;
import cc.iotkit.data.dao.HomeRepository;
import cc.iotkit.data.model.AlertConfigMapper;
import cc.iotkit.data.model.AlertRecordMapper;
import cc.iotkit.data.model.HomeMapper;
import cc.iotkit.data.model.TbAlertConfig;
import cc.iotkit.data.model.TbAlertRecord;
import cc.iotkit.model.Paging;
import cc.iotkit.model.alert.AlertRecord;
import cc.iotkit.model.space.Home;
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
public class AlertRecordDataImpl implements IAlertRecordData {

  @Autowired
  private AlertRecordRepository alertRecordRepository;


  @Override
  public AlertRecord findById(String s) {
    return null;
  }

  @Override
  public AlertRecord save(AlertRecord data) {
    return null;
  }

  @Override
  public AlertRecord add(AlertRecord data) {
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
  public List<AlertRecord> findAll() {
    return null;
  }

  @Override
  public Paging<AlertRecord> findAll(int page, int size) {
    return null;
  }


  @Override
  public Paging<AlertRecord> selectAlertConfigPage(PageRequest<AlertRecord> request) {
    Pageable pageable = org.springframework.data.domain.PageRequest.of(request.getPageNo(),
        request.getPageSize(), Sort.Direction.DESC, "alartTime");
    Page<TbAlertRecord> alertRecordPage = alertRecordRepository.findAll(
        Example.of(AlertRecordMapper.M.toVo(request.getData())), pageable);
    return new Paging<>(alertRecordPage.getTotalElements(),
        AlertRecordMapper.toDto(alertRecordPage.getContent()));
  }
}
