package cc.iotkit.data.service;

import cc.iotkit.data.ISpaceDeviceData;
import cc.iotkit.model.Paging;
import cc.iotkit.model.space.SpaceDevice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaceDeviceDataImpl implements ISpaceDeviceData {
    @Override
    public List<SpaceDevice> findByUidOrderByUseAtDesc(String uid) {
        return null;
    }

    @Override
    public List<SpaceDevice> findByUidOrderByAddAtDesc(String uid) {
        return null;
    }

    @Override
    public List<SpaceDevice> findBySpaceIdOrderByAddAtDesc(String spaceId) {
        return null;
    }

    @Override
    public List<SpaceDevice> findByUidAndSpaceIdOrderByAddAtDesc(String uid, String spaceId) {
        return null;
    }

    @Override
    public SpaceDevice findByDeviceId(String deviceId) {
        return null;
    }

    @Override
    public SpaceDevice findByDeviceIdAndUid(String deviceId, String uid) {
        return null;
    }

    @Override
    public List<SpaceDevice> findByUid(String uid) {
        return null;
    }

    @Override
    public Paging<SpaceDevice> findByUid(String uid, int page, int size) {
        return null;
    }

    @Override
    public long countByUid(String uid) {
        return 0;
    }

    @Override
    public SpaceDevice findById(String s) {
        return null;
    }

    @Override
    public SpaceDevice save(SpaceDevice data) {
        return null;
    }

    @Override
    public SpaceDevice add(SpaceDevice data) {
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
    public List<SpaceDevice> findAll() {
        return null;
    }

    @Override
    public Paging<SpaceDevice> findAll(int page, int size) {
        return null;
    }
}
