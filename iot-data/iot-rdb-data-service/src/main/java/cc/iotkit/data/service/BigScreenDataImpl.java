package cc.iotkit.data.service;

import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.data.IBigScreenData;
import cc.iotkit.data.dao.BigScreenApiMappingRepository;
import cc.iotkit.data.dao.BigScreenApiRepository;
import cc.iotkit.data.dao.BigScreenRepository;
import cc.iotkit.data.model.BigScreenMapper;
import cc.iotkit.data.model.TbBigScreen;
import cc.iotkit.data.model.TbBigScreenApiMapping;
import cc.iotkit.model.Paging;
import cc.iotkit.model.screen.BigScreen;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @Author：tfd
 * @Date：2023/5/6 15:57
 */
@Primary
@Service
public class BigScreenDataImpl implements IBigScreenData {

    @Autowired
    private BigScreenRepository bigScreenRepository;

    @Autowired
    private BigScreenApiRepository bigScreenApiRepository;

    @Autowired
    private BigScreenApiMappingRepository bigScreenApiMappingRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<BigScreen> findByUid(String uid) {
        return BigScreenMapper.toDto(bigScreenRepository.findByUid(uid));
    }

    @Override
    public Paging<BigScreen> findByUid(String uid, int page, int size) {
        Page<TbBigScreen> paged = bigScreenRepository.findByUid(uid,
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                BigScreenMapper.toDto(paged.getContent()));
    }

    @Override
    public long countByUid(String uid) {
        return bigScreenRepository.countByUid(uid);
    }

    @Override
    public BigScreen findById(String s) {
        return BigScreenMapper.M.toDto(bigScreenRepository.findById(s).orElse(null));
    }

    @Override
    public BigScreen findByUidAndIsDefault(String uid, boolean current) {
        return BigScreenMapper.M.toDto(bigScreenRepository.findByUidAndIsDefault(uid, current));
    }

    @Override
    @Transactional
    public BigScreen save(BigScreen data) {
        TbBigScreen vo = bigScreenRepository.findById(data.getId()).orElse(null);
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
            data.setCreateAt(System.currentTimeMillis());
        }
        if(vo == null){
            vo =new TbBigScreen();
        }
        ReflectUtil.copyNoNulls(data, vo);
        bigScreenRepository.save(vo);
        List<TbBigScreenApiMapping> bigScreenApiMappings = bigScreenApiMappingRepository.findByScreenId(data.getId());
        if(bigScreenApiMappings.size()>0){
            bigScreenApiMappings.forEach((bigScreenApiMapping)->{
                bigScreenApiRepository.deleteById(bigScreenApiMapping.getApiId());
            });
            bigScreenApiMappingRepository.deleteByScreenId(data.getId());
        }
//        if(data.getBigScreenApis().size()>0){
//            data.getBigScreenApis().forEach((item) ->{
//                if (StringUtils.isBlank(item.getId())) {
//                    item.setId(UUID.randomUUID().toString());
//                    item.setCreateAt(System.currentTimeMillis());
//                    item.setUid(data.getUid());
//                }
//                bigScreenApiMappingRepository.save(new TbBigScreenApiMapping(
//                        UUID.randomUUID().toString(),
//                        data.getId(),
//                        item.getId()
//                ));
//                bigScreenApiRepository.save(BigScreenApiMapper.M.toVo(item));
//            });
//        }
        return data;
    }

    @Override
    public BigScreen add(BigScreen data) {
        return save(data);
    }

    @Override
    public void deleteById(String s) {
        bigScreenRepository.deleteById(s);
    }

    @Override
    public long count() {
        return bigScreenRepository.count();
    }

    @Override
    public List<BigScreen> findAll() {
        return BigScreenMapper.toDto(bigScreenRepository.findAll());
    }

    @Override
    public Paging<BigScreen> findAll(int page, int size) {
        Page<TbBigScreen> paged = bigScreenRepository
                .findAll(Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                BigScreenMapper.toDto(paged.getContent()));
    }
}
