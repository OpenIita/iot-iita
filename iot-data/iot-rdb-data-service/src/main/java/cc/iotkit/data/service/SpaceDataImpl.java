package cc.iotkit.data.service;

import cc.iotkit.data.ISpaceData;
import cc.iotkit.data.dao.SpaceRepository;
import cc.iotkit.data.model.SpaceMapper;
import cc.iotkit.data.model.TbSpace;
import cc.iotkit.model.Paging;
import cc.iotkit.model.space.Space;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Primary
@Service
public class SpaceDataImpl implements ISpaceData {

    @Autowired
    private SpaceRepository spaceRepository;

    @Override
    public List<Space> findByUidOrderByCreateAtDesc(String uid) {
        return SpaceMapper.toDto(spaceRepository.findByUidOrderByCreateAtDesc(uid));
    }

    @Override
    public List<Space> findByUidAndHomeIdOrderByCreateAtDesc(String uid, String homeId) {
        return SpaceMapper.toDto(spaceRepository.findByUidAndHomeIdOrderByCreateAtDesc(uid, homeId));
    }

    @Override
    public List<Space> findByHomeId(String homeId) {
        return SpaceMapper.toDto(spaceRepository.findByHomeId(homeId));
    }

    @Override
    public List<Space> findByUid(String uid) {
        return SpaceMapper.toDto(spaceRepository.findByUid(uid));
    }

    @Override
    public Paging<Space> findByUid(String uid, int page, int size) {
        Page<TbSpace> paged = spaceRepository.findByUid(uid,
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                SpaceMapper.toDto(paged.getContent()));
    }

    @Override
    public long countByUid(String uid) {
        return 0;
    }

    @Override
    public Space findById(String s) {
        return SpaceMapper.M.toDto(spaceRepository.findById(s).orElse(null));
    }

    @Override
    public Space save(Space data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        spaceRepository.save(SpaceMapper.M.toVo(data));
        return data;
    }

    @Override
    public Space add(Space data) {
        return save(data);
    }

    @Override
    public void deleteById(String s) {
        spaceRepository.deleteById(s);
    }

    @Override
    public long count() {
        return spaceRepository.count();
    }

    @Override
    public List<Space> findAll() {
        return SpaceMapper.toDto(spaceRepository.findAll());
    }

    @Override
    public Paging<Space> findAll(int page, int size) {
        return new Paging<>();
    }
}
