package cc.iotkit.data.service;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.SysConfigRepository;
import cc.iotkit.data.model.TbSysConfig;
import cc.iotkit.data.system.ISysConfigData;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.system.SysConfig;
import java.util.List;

import io.github.linpeilie.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class SysConfigDataImpl implements ISysConfigData {

    @Autowired
    private SysConfigRepository alertConfigRepository;


    @Override
    public SysConfig findById(Long id) {
       TbSysConfig tbSysConfig =  alertConfigRepository.findById(id).orElseThrow();
        return MapstructUtils.convert(tbSysConfig,SysConfig.class);
    }

    @Override
    public SysConfig save(SysConfig data) {
        alertConfigRepository.save(MapstructUtils.convert(data,TbSysConfig.class));
        return data;
    }

    @Override
    public SysConfig add(SysConfig data) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteByIds(Long[] longs) {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<SysConfig> findAll() {
        return null;
    }

    @Override
    public Paging<SysConfig> findAll(int page, int size) {
        return null;
    }


    @Override
    public SysConfig selectPageConfigList(SysConfig query) {
        return null;
    }
}
