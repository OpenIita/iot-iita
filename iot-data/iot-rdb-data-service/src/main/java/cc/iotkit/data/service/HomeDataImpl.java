package cc.iotkit.data.service;

import cc.iotkit.data.IHomeData;
import cc.iotkit.data.dao.HomeRepository;
import cc.iotkit.data.model.HomeMapper;
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
        return HomeMapper.M.toDto(homeRepository.findByUidAndCurrent(uid, current));
    }

    @Override
    public Home findByUidAndId(String uid, String id) {
        return HomeMapper.M.toDto(homeRepository.findByUidAndId(uid, id));
    }

    @Override
    public List<Home> findByUid(String uid) {
        return HomeMapper.toDto(homeRepository.findByUid(uid));
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
        return HomeMapper.M.toDto(homeRepository.findById(s).orElse(null));
    }

    @Override
    public Home save(Home data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        homeRepository.save(HomeMapper.M.toVo(data));
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
    public long count() {
        return homeRepository.count();
    }

    @Override
    public List<Home> findAll() {
        return HomeMapper.toDto(homeRepository.findAll());
    }

    @Override
    public Paging<Home> findAll(int page, int size) {
        return new Paging<>();
    }
}
