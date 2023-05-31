package cc.iotkit.data.service;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.SysTenantRepository;
import cc.iotkit.data.system.ISysTenantData;
import cc.iotkit.model.system.SysUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @Author：tfd
 * @Date：2023/5/31 9:45
 */
@Primary
@Service
@RequiredArgsConstructor
public class SysTenantDataImpl implements ISysTenantData {

    private SysTenantRepository sysTenantRepository;

    @Override
    public SysUser findById(Long aLong) {
        return MapstructUtils.convert(sysTenantRepository.findById(aLong),SysUser.class);
    }
}
