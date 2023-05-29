package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.manager.ISpaceData;
import cc.iotkit.data.dao.SpaceRepository;
import cc.iotkit.data.model.TbSpace;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.space.Space;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Primary
@Service
public class SpaceDataImpl implements ISpaceData {

    @Autowired
    private SpaceRepository spaceRepository;

    @Override
    public List<Space> findByUidOrderByCreateAtDesc(String uid) {
        return MapstructUtils.convert(spaceRepository.findByUidOrderByCreateAtDesc(uid), Space.class);
    }

    @Override
    public List<Space> findByUidAndHomeIdOrderByCreateAtDesc(String uid, String homeId) {
        return MapstructUtils.convert(spaceRepository.findByUidAndHomeIdOrderByCreateAtDesc(uid, homeId), Space.class);
    }

    @Override
    public List<Space> findByHomeId(String homeId) {
        return MapstructUtils.convert(spaceRepository.findByHomeId(homeId), Space.class);
    }

    @Override
    public List<Space> findByUid(String uid) {
        return MapstructUtils.convert(spaceRepository.findByUid(uid), Space.class);
    }

    @Override
    public Paging<Space> findByUid(String uid, int page, int size) {
        Page<TbSpace> paged = spaceRepository.findByUid(uid,
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                MapstructUtils.convert(paged.getContent(), Space.class));
    }

    @Override
    public long countByUid(String uid) {
        return 0;
    }

    @Override
    public Space findById(String s) {
        return MapstructUtils.convert(spaceRepository.findById(s).orElse(null), Space.class);
    }

    @Override
    public List<Space> findByIds(Collection<String> id) {
        return null;
    }

    @Override
    public Space save(Space data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        spaceRepository.save(MapstructUtils.convert(data, TbSpace.class));
        return data;
    }

    @Override
    public void batchSave(List<Space> data) {

    }

    @Override
    public void deleteById(String s) {
        spaceRepository.deleteById(s);
    }

    @Override
    public void deleteByIds(Collection<String> strings) {

    }



    @Override
    public long count() {
        return spaceRepository.count();
    }

    @Override
    public List<Space> findAll() {
        return MapstructUtils.convert(spaceRepository.findAll(), Space.class);
    }

    @Override
    public Paging<Space> findAll(PageRequest<Space> pageRequest) {
        return null;
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
