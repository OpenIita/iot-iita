package cc.iotkit.data.service;

import cc.iotkit.data.ISpaceData;
import cc.iotkit.model.Paging;
import cc.iotkit.model.space.Space;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaceDataImpl implements ISpaceData {
    @Override
    public List<Space> findByUidOrderByCreateAtDesc(String uid) {
        return null;
    }

    @Override
    public List<Space> findByUidAndHomeIdOrderByCreateAtDesc(String uid, String homeId) {
        return null;
    }

    @Override
    public List<Space> findByHomeId(String homeId) {
        return null;
    }

    @Override
    public List<Space> findByUid(String uid) {
        return null;
    }

    @Override
    public Paging<Space> findByUid(String uid, int page, int size) {
        return null;
    }

    @Override
    public long countByUid(String uid) {
        return 0;
    }

    @Override
    public Space findById(String s) {
        return null;
    }

    @Override
    public Space save(Space data) {
        return null;
    }

    @Override
    public Space add(Space data) {
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
    public List<Space> findAll() {
        return null;
    }

    @Override
    public Paging<Space> findAll(int page, int size) {
        return null;
    }
}
