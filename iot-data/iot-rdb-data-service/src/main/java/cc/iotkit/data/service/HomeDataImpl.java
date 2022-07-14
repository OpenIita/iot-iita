package cc.iotkit.data.service;

import cc.iotkit.data.IHomeData;
import cc.iotkit.model.Paging;
import cc.iotkit.model.space.Home;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeDataImpl implements IHomeData {
    @Override
    public Home findByUidAndCurrent(String uid, boolean current) {
        return null;
    }

    @Override
    public Home findByUidAndId(String uid, String id) {
        return null;
    }

    @Override
    public List<Home> findByUid(String uid) {
        return null;
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
        return null;
    }

    @Override
    public Home save(Home data) {
        return null;
    }

    @Override
    public Home add(Home data) {
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
    public List<Home> findAll() {
        return null;
    }

    @Override
    public Paging<Home> findAll(int page, int size) {
        return null;
    }
}
