package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.constant.Constants;
import cc.iotkit.data.manager.ISpaceData;
import cc.iotkit.data.cache.SpaceCacheEvict;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.space.Space;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Qualifier("spaceDataCache")
public class SpaceDataCache implements ISpaceData {

    @Autowired
    private ISpaceData spaceData;
    @Autowired
    private SpaceCacheEvict spaceCacheEvict;

    @Override
    public List<Space> findByUidOrderByCreateAtDesc(String uid) {
        return spaceData.findByUidOrderByCreateAtDesc(uid);
    }

    @Override
    public List<Space> findByUidAndHomeIdOrderByCreateAtDesc(String uid, String homeId) {
        return spaceData.findByUidAndHomeIdOrderByCreateAtDesc(uid, homeId);
    }

    @Override
    public List<Space> findByHomeId(String homeId) {
        return spaceData.findByHomeId(homeId);
    }

    @Override
    public List<Space> findByUid(String uid) {
        return spaceData.findByUid(uid);
    }

    @Override
    public Paging<Space> findByUid(String uid, int page, int size) {
        return spaceData.findByUid(uid, page, size);
    }

    @Override
    public long countByUid(String uid) {
        return spaceData.countByUid(uid);
    }

    @Override
    @Cacheable(value = Constants.CACHE_SPACE, key = "#root.method.name+#s", unless = "#result == null")
    public Space findById(String s) {
        return spaceData.findById(s);
    }

    @Override
    public List<Space> findByIds(Collection<String> id) {
        return null;
    }

    @Override
    public Space save(Space data) {
        data = spaceData.save(data);
        spaceCacheEvict.findById(data.getId());
        return data;
    }

    @Override
    public void batchSave(List<Space> data) {

    }

    @Override
    public void deleteById(String s) {
        spaceData.deleteById(s);
    }

    @Override
    public void deleteByIds(Collection<String> strings) {

    }

    @Override
    public long count() {
        return spaceData.count();
    }

    @Override
    public List<Space> findAll() {
        return spaceData.findAll();
    }

    @Override
    public Paging<Space> findAll(PageRequest<Space> pageRequest) {
        return spaceData.findAll(pageRequest);
    }

    @Override
    public List<Space> findAllByCondition(Space data) {
        return null;
    }

    @Override
    public Space findOneByCondition(Space data) {
        return null;
    }

}
