package cc.iotkit.data.service;

import cc.iotkit.data.IOtaDeviceData;
import cc.iotkit.data.dao.IOtaDeviceRepository;
import cc.iotkit.data.model.OtaDeviceMapper;
import cc.iotkit.data.model.TbOtaDevice;
import cc.iotkit.model.Paging;
import cc.iotkit.model.ota.OtaDevice;
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
 * @Date: 2023/5/25 23:41
 * @Description:
 */
@Primary
@Service
public class IOtaDeviceDataImpl implements IOtaDeviceData {

    @Resource
    private IOtaDeviceRepository iOtaDeviceRepository;

    @Override
    public OtaDevice findById(String id) {
        return OtaDeviceMapper.M.toDto(iOtaDeviceRepository.findById(id).orElse(null));
    }

    @Override
    public OtaDevice save(OtaDevice data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        iOtaDeviceRepository.save(OtaDeviceMapper.M.toVo(data));
        return data;
    }

    @Override
    public OtaDevice add(OtaDevice data) {
        data.setCreateAt(System.currentTimeMillis());
        return save(data);
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
        return iOtaDeviceRepository.findAll().stream().map(OtaDeviceMapper.M::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Paging<OtaDevice> findAll(int page, int size) {
        Page<TbOtaDevice> tbOtaPackages = iOtaDeviceRepository.findAll(Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(
                tbOtaPackages.getTotalElements(),
                tbOtaPackages.getContent()
                        .stream().map(OtaDeviceMapper.M::toDto)
                        .collect(Collectors.toList())
        );
    }
}
