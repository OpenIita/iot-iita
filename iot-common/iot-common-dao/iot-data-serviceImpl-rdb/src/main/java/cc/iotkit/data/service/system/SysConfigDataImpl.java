package cc.iotkit.data.service.system;

import cc.iotkit.data.dao.system.SysConfigRepository;
import cc.iotkit.data.system.ISysConfigData;
import cc.iotkit.model.Paging;
import cc.iotkit.model.system.SysConfig;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class SysConfigDataImpl implements ISysConfigData {

    @Autowired
    private SysConfigRepository alertConfigRepository;


    @Override
    public SysConfig findById(Long aLong) {
        return null;
    }

    @Override
    public SysConfig save(SysConfig data) {
        return null;
    }

    @Override
    public SysConfig add(SysConfig data) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

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
}
