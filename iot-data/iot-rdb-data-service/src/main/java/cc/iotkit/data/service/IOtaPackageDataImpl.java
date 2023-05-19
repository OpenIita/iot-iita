package cc.iotkit.data.service;

import cc.iotkit.data.IOtaPackageData;
import cc.iotkit.data.dao.IOtaPackageRepository;
import cc.iotkit.data.model.OtaPackageMapper;
import cc.iotkit.data.model.TbOtaPackage;
import cc.iotkit.model.Paging;
import cc.iotkit.model.ota.OtaPackage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author: 石恒
 * @Date: 2023/5/19 21:53
 * @Description:
 */
@Primary
@Service
public class IOtaPackageDataImpl implements IOtaPackageData {

    @Resource
    private IOtaPackageRepository iOtaPackageRepository;

    @Override
    public OtaPackage findById(String id) {
        return OtaPackageMapper.M.toDto(iOtaPackageRepository.findById(id).orElse(null));
    }

    @Override
    public OtaPackage save(OtaPackage data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        iOtaPackageRepository.save(OtaPackageMapper.M.toVo(data));
        return data;
    }

    @Override
    public OtaPackage add(OtaPackage data) {
        data.setCreateAt(System.currentTimeMillis());
        return save(data);
    }

    @Override
    public void deleteById(String id) {
        iOtaPackageRepository.deleteById(id);
    }

    @Override
    public long count() {
        return iOtaPackageRepository.count();
    }

    @Override
    public List<OtaPackage> findAll() {
        return iOtaPackageRepository.findAll().stream().map(OtaPackageMapper.M::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Paging<OtaPackage> findAll(int page, int size) {
        Page<TbOtaPackage> tbOtaPackages = iOtaPackageRepository.findAll(Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(
                tbOtaPackages.getTotalElements(),
                tbOtaPackages.getContent()
                        .stream().map(OtaPackageMapper.M::toDto)
                        .collect(Collectors.toList())
        );
    }
}
