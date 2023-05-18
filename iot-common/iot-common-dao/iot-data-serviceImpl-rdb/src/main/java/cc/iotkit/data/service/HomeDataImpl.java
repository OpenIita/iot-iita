package cc.iotkit.data.service;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.manager.IHomeData;
import cc.iotkit.data.dao.HomeRepository;
import cc.iotkit.data.model.TbHome;
import cc.iotkit.model.Paging;
import cc.iotkit.model.space.Home;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Primary
@Service
public class HomeDataImpl implements IHomeData {

    @Autowired
    private HomeRepository homeRepository;

    @Override
    public Home findByUidAndCurrent(String uid, boolean current) {
        return MapstructUtils.convert(homeRepository.findByUidAndCurrent(uid, current), Home.class);
    }

    @Override
    public Home findByUidAndId(String uid, String id) {
        return MapstructUtils.convert(homeRepository.findByUidAndId(uid, id), Home.class);
    }

    @Override
    public List<Home> findByUid(String uid) {
        return MapstructUtils.convert(homeRepository.findByUid(uid), Home.class);
    }

    @Override
    public Paging<Home> findByUid(String uid, int page, int size) {
        return null;
    }

    @Override
    public long countByUid(String uid) {
        return 0;
    }

    @Override
    public Home findById(String s) {
        return MapstructUtils.convert(homeRepository.findById(s).orElse(null), Home.class);
    }

    @Override
    public Home save(Home data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        homeRepository.save(MapstructUtils.convert(data, TbHome.class));
        return data;
    }

    @Override
    public Home add(Home data) {
        return save(data);
    }

    @Override
    public void deleteById(String s) {
        homeRepository.deleteById(s);
    }

    @Override
    public void deleteByIds(String[] strings) {

    }

    @Override
    public long count() {
        return homeRepository.count();
    }

    @Override
    public List<Home> findAll() {
        return MapstructUtils.convert(homeRepository.findAll(), Home.class);
    }

    @Override
    public Paging<Home> findAll(int page, int size) {
        return new Paging<>();
    }
}
