package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.IOtaDeviceRepository;
import cc.iotkit.data.manager.IOtaDeviceData;
import cc.iotkit.data.model.TbOtaDevice;
import cc.iotkit.model.ota.OtaDevice;
import cc.iotkit.model.space.Home;
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
 * @Date: 2023/5/25 23:41
 * @Description:
 */
@Primary
@Service
public class IOtaDeviceDataImpl implements IOtaDeviceData, IJPACommData<OtaDevice, String> {

    @Resource
    private IOtaDeviceRepository iOtaDeviceRepository;

    @Override
    public JpaRepository getBaseRepository() {
        return iOtaDeviceRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbOtaDevice.class;
    }

    @Override
    public Class getTClass() {
        return OtaDevice.class;
    }

    @Override
    public OtaDevice save(OtaDevice data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        iOtaDeviceRepository.save(MapstructUtils.convert(data, TbOtaDevice.class));
        return data;
    }

    @Override
    public OtaDevice findById(String id) {
        return MapstructUtils.convert(iOtaDeviceRepository.findById(id).orElse(null), OtaDevice.class);
    }

    @Override
    public void deleteById(String id) {
        iOtaDeviceRepository.deleteById(id);
    }

    @Override
    public long count() {
        return iOtaDeviceRepository.count();
    }

    @Override
    public List<OtaDevice> findAll() {
        return iOtaDeviceRepository.findAll().stream().map(e -> MapstructUtils.convert(e, OtaDevice.class))
                .collect(Collectors.toList());
    }

    @Override
    public Paging<OtaDevice> findAll(PageRequest<OtaDevice> pageRequest) {
        Page<TbOtaDevice> tbOtaPackages = iOtaDeviceRepository.findAll(Pageable.ofSize(pageRequest.getPageSize()).withPage(pageRequest.getPageNum() - 1));
        return new Paging<>(
                tbOtaPackages.getTotalElements(),
                tbOtaPackages.getContent()
                        .stream().map(e -> MapstructUtils.convert(e, OtaDevice.class))
                        .collect(Collectors.toList())
        );
    }
}
