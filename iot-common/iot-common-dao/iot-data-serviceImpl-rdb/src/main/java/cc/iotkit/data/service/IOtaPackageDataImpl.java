package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.IOtaPackageRepository;
import cc.iotkit.data.manager.IOtaPackageData;
import cc.iotkit.data.model.TbOtaDevice;
import cc.iotkit.data.model.TbOtaPackage;
import cc.iotkit.model.ota.OtaDevice;
import cc.iotkit.model.ota.OtaPackage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
public class IOtaPackageDataImpl implements IOtaPackageData, IJPACommData<OtaPackage, String> {

    @Resource
    private IOtaPackageRepository iOtaPackageRepository;

    @Override
    public List<OtaPackage> findByVersionGreaterThan(String version) {
        return null;
    }

    @Override
    public JpaRepository getBaseRepository() {
        return iOtaPackageRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbOtaPackage.class;
    }

    @Override
    public Class getTClass() {
        return OtaPackage.class;
    }

    @Override
    public OtaPackage save(OtaPackage data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        iOtaPackageRepository.save(MapstructUtils.convert(data, TbOtaPackage.class));
        return data;
    }

    @Override
    public OtaPackage findById(String id) {
        return MapstructUtils.convert(iOtaPackageRepository.findById(id).orElse(null), OtaPackage.class);
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
        return iOtaPackageRepository.findAll().stream().map(e -> MapstructUtils.convert(e, OtaPackage.class)).collect(Collectors.toList());
    }

    @Override
    public Paging<OtaPackage> findAll(PageRequest<OtaPackage> pageRequest) {
        Page<TbOtaPackage> tbOtaPackages = iOtaPackageRepository.findAll(Pageable.ofSize(pageRequest.getPageSize()).withPage(pageRequest.getPageNum() - 1));
        return new Paging<>(tbOtaPackages.getTotalElements(), tbOtaPackages.getContent().stream().map(e -> MapstructUtils.convert(e, OtaPackage.class)).collect(Collectors.toList()));
    }
}
