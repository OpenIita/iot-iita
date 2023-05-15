package cc.iotkit.data.service;

import cc.iotkit.data.IBigScreenApiData;
import cc.iotkit.data.dao.BigScreenApiMappingRepository;
import cc.iotkit.data.dao.BigScreenApiRepository;
import cc.iotkit.data.model.BigScreenApiMapper;
import cc.iotkit.data.model.TbBigScreenApi;
import cc.iotkit.data.model.TbBigScreenApiMapping;
import cc.iotkit.model.Paging;
import cc.iotkit.model.screen.BigScreen;
import cc.iotkit.model.screen.BigScreenApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author：tfd
 * @Date：2023/5/6 15:57
 */
@Primary
@Service
public class BigScreenApiDataImpl implements IBigScreenApiData {

    @Autowired
    private BigScreenApiRepository bigScreenApiRepository;

    @Autowired
    private BigScreenApiMappingRepository bigScreenApiMappingRepository;

    @Override
    public List<BigScreenApi> findByUid(String uid) {
        return BigScreenApiMapper.toDto(bigScreenApiRepository.findByUid(uid));
    }

    @Override
    public List<BigScreenApi> findByScreenId(String id) {
        List<BigScreenApi> screenApis = new ArrayList<>();
        List<TbBigScreenApiMapping> screenApiMapping = bigScreenApiMappingRepository.findByScreenId(id);
        if (screenApiMapping.size() > 0) {
            screenApis = screenApiMapping.stream().map(o -> BigScreenApiMapper.M.toDto(bigScreenApiRepository.findById(o.getApiId()).orElse(null))).collect(Collectors.toList());
        }
        return screenApis;
    }

    @Override
    public Paging<BigScreenApi> findByUid(String uid, int page, int size) {
        Page<TbBigScreenApi> paged = bigScreenApiRepository.findByUid(uid,
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                BigScreenApiMapper.toDto(paged.getContent()));
    }

    @Override
    @Transactional
    public void saveApiList(BigScreen screen, List<BigScreenApi> screenApis) {
        List<TbBigScreenApiMapping> bigScreenApiMappings = bigScreenApiMappingRepository.findByScreenId(screen.getId());
        if (bigScreenApiMappings.size() > 0) {
            bigScreenApiMappings.forEach((bigScreenApiMapping) -> {
                bigScreenApiRepository.deleteById(bigScreenApiMapping.getApiId());
            });
            bigScreenApiMappingRepository.deleteByScreenId(screen.getId());
        }
        screenApis.forEach((item) -> {
            if (StringUtils.isBlank(item.getId())) {
                item.setId(UUID.randomUUID().toString());
            }
            item.setCreateAt(System.currentTimeMillis());
            item.setUid(screen.getUid());
            bigScreenApiMappingRepository.save(new TbBigScreenApiMapping(
                    UUID.randomUUID().toString(),
                    screen.getId(),
                    item.getId()
            ));
            bigScreenApiRepository.save(BigScreenApiMapper.M.toVo(item));
        });
    }

    @Override
    public long countByUid(String uid) {
        return bigScreenApiRepository.countByUid(uid);
    }

    @Override
    public BigScreenApi findById(String s) {
        return BigScreenApiMapper.M.toDto(bigScreenApiRepository.findById(s).orElse(null));
    }

    @Override
    public BigScreenApi save(BigScreenApi data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
            data.setCreateAt(System.currentTimeMillis());
        }
        bigScreenApiRepository.save(BigScreenApiMapper.M.toVo(data));
        return data;
    }

    @Override
    public BigScreenApi add(BigScreenApi data) {
        return save(data);
    }

    @Override
    public void deleteById(String s) {
        bigScreenApiRepository.deleteById(s);
    }

    @Override
    public long count() {
        return bigScreenApiRepository.count();
    }

    @Override
    public List<BigScreenApi> findAll() {
        return BigScreenApiMapper.toDto(bigScreenApiRepository.findAll());
    }

    @Override
    public Paging<BigScreenApi> findAll(int page, int size) {
        Page<TbBigScreenApi> paged = bigScreenApiRepository
                .findAll(Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                BigScreenApiMapper.toDto(paged.getContent()));
    }
}
