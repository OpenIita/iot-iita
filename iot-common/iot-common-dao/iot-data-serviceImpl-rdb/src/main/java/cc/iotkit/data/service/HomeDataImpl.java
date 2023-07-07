package cc.iotkit.data.service;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.HomeRepository;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.IHomeData;
import cc.iotkit.data.model.TbHome;
import cc.iotkit.model.space.Home;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Primary
@Service
public class HomeDataImpl implements IHomeData, IJPACommData<Home, String> {

    @Autowired
    private HomeRepository homeRepository;


    @Override
    public JpaRepository getBaseRepository() {
        return homeRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbHome.class;
    }

    @Override
    public Class getTClass() {
        return Home.class;
    }

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




}
